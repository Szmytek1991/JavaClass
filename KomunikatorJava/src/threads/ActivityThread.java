package threads;

import windows.MainWindow;

public class ActivityThread implements Runnable
{

	Thread runner;
	public ActivityThread() {
		runner = new Thread(); // (1) Create a new thread.
		runner.start(); // (2) Start the thread.
	}
	public ActivityThread(String threadName) {
		runner = new Thread(threadName); // (1) Create a new thread.
		//System.out.println(runner.getName());
		MainWindow.activitythread();
		runner.start(); // (2) Start the thread.
	}
	public void run() {
		MainWindow.activitythread();
		//Display info about this particular thread
		System.out.println(Thread.currentThread());
	}
}
