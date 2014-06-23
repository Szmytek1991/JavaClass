package threads;

import windows.MainWindow;

public class ConversationThread implements Runnable
{

	Thread runner;
	public ConversationThread() {
		runner = new Thread(); // (1) Create a new thread.
		runner.start(); // (2) Start the thread.
	}
	public ConversationThread(String threadName) {
		runner = new Thread(threadName); // (1) Create a new thread.
		runner.start(); // (2) Start the thread.
	}
	public void run() {
		try {
			MainWindow.conversationthread();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Display info about this particular thread
		System.out.println(Thread.currentThread());
	}
}