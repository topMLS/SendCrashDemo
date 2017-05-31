package demo.sendcrashdemo;


public class SenderRunable implements Runnable{
	private String user;
	private String password;
	private String subject;
	private String body;
	private String receiver;
	private MailSender sender;
	private String attachment;

	public SenderRunable(String user, String password,String mailhostPort,String mailhost) {
		this.user = user;
		this.password = password;

		if (mailhost!=null) {//默认为smtp.exmail.qq.com
			sender.setMailhost(mailhost);
		}
		if (mailhostPort!=null) {//默认为465
			sender.setMailhostPort(mailhostPort);
		}
		sender = new MailSender(user, password);
	}

	public void setMail(String subject, String body, String receiver,String attachment) {
		this.subject = subject;
		this.body = body;
		this.receiver = receiver;
		this.attachment=attachment;
	}

	public void run() {
		// TODO Auto-generated method stub
		try {
				sender.sendMail(subject, body, user, receiver,attachment);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
