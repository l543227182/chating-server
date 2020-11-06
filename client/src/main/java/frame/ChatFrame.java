package frame;

import com.google.gson.Gson;
import decode.MsgDecode;
import domain.ClientAction;
import domain.MsgObj;
import server.ClientService;
import server.MsgService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.util.List;

/**
 *
 */
public class ChatFrame {

    private final JTextArea readContext = new JTextArea(18, 30);// 显示消息文本框
    private final JTextArea writeContext = new JTextArea(6, 30);// 发送消息文本框

    private final DefaultListModel modle = new DefaultListModel();// 用户列表模型
    private final JList list = new JList(modle);// 用户列表

    private final JButton btnSend = new JButton("发送");// 发送消息按钮
    private final JButton btnClose = new JButton("关闭");// 关闭聊天窗口按钮

    private final JFrame frame = new JFrame("frame.ChatFrame");// 窗体界面

    private final String uname;// 用户姓名

    private final ClientService service;// 用于与服务器交互

    private boolean isRun = false;// 是否运行

    public ChatFrame(ClientService service, String uname) {
        this.isRun = true;
        this.uname = uname;
        this.service = service;
    }

    // 初始化界面控件及事件
    private void init() {
        frame.setLayout(null);
        frame.setTitle(uname + " 聊天窗口");
        frame.setSize(500, 500);
        frame.setLocation(400, 200);
        //设置可关闭
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //不能改变窗体大小
        frame.setResizable(false);
        //聊天消息显示区带滚动条
        JScrollPane readScroll = new JScrollPane(readContext);
        readScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        frame.add(readScroll);
        //消息编辑区带滚动条
        JScrollPane writeScroll = new JScrollPane(writeContext);
        writeScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        frame.add(writeScroll);
        frame.add(list);
        frame.add(btnSend);
        frame.add(btnClose);
        readScroll.setBounds(10, 10, 320, 300);
        readContext.setBounds(0, 0, 320, 300);
        readContext.setEditable(false);//设置为不可编辑
        readContext.setLineWrap(true);// 自动换行
        writeScroll.setBounds(10, 315, 320, 100);
        writeContext.setBounds(0, 0, 320, 100);
        writeContext.setLineWrap(true);// 自动换行
        list.setBounds(340, 10, 140, 445);
        btnSend.setBounds(150, 420, 80, 30);
        btnClose.setBounds(250, 420, 80, 30);
        //窗体关闭事件
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                isRun = false;
                service.sendMsg(MsgService.sayBye(uname));
                System.exit(0);
            }
        });

        //发送按钮事件
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = writeContext.getText().trim();
                if (msg.length() > 0) {
                    service.sendMsg(MsgService.saySomething(uname, writeContext.getText()));
                }
                //发送消息后，去掉编辑区文本，并获得光标焦点
                writeContext.setText(null);
                writeContext.requestFocus();
            }
        });

        //关闭按钮事件
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isRun = false;
                service.sendMsg((MsgService.sayBye(uname)));
                System.exit(0);
            }
        });

        //右边名称列表选择事件
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // JOptionPane.showMessageDialog(null,
                // list.getSelectedValue().toString());
            }
        });

        //消息编辑区键盘按键事件
        writeContext.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            //按下键盘按键后释放
            @Override
            public void keyReleased(KeyEvent e) {
                //按下enter键发送消息
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String msg = writeContext.getText().trim();
                    if (msg.length() > 0) {
                        service.sendMsg((MsgService.saySomething(uname, writeContext.getText())));
                    }
                    writeContext.setText(null);
                    writeContext.requestFocus();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub

            }
        });
    }

    // 此线程类用于轮询读取服务器发送的消息
    private class MsgThread extends Thread {
        @Override
        public void run() {
            Gson gson = new Gson();
            while (isRun) {
                String msg = service.receiveMsg();
                if (msg != null) {
                    MsgObj msgObj = MsgDecode.decodeMsg(msg);
                    //若是名称列表数据，则更新聊天窗体右边的列表
                    if (ClientAction.REFRESH_USER.equals(msgObj.getClientAction())) {
                        List<String> userNames = gson.fromJson(msgObj.getMsg(), List.class);
                        modle.removeAllElements();
                        for (int i = 0; i < userNames.size(); i++) {
                            modle.addElement(userNames.get(i).trim());
                        }
                    } else {
                        //将聊天数据设置到聊天消息显示区
                        String str = readContext.getText() + msgObj.getMsg();
                        readContext.setText(str);
                        readContext.selectAll();//保持滚动条在最下面
                    }
                }
            }
        }
    }

    // 显示界面
    public void show() {
        this.init();
        service.sendMsg(MsgService.openMsg(uname));
        MsgThread msgThread = new MsgThread();
        msgThread.start();
        this.frame.setVisible(true);
    }
}