package controllers;
import windows.*;
public class WindowController {
	static MainWindow mainwindow;
	static CreateUser createuser;
	static LoginWindow loginwindow;
	static AboutBox aboutbox;
	static AddFriend addfriend;

	public static void main(String[] args) 
	{

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {

				showloginwindow();
			}
		});

	}
	
	@SuppressWarnings("deprecation")
	public static void showloginwindow()
	{
		loginwindow = new LoginWindow();
		loginwindow.show(true);
	}
	@SuppressWarnings("deprecation")
	public static void showusercreatorwindow()
	{
		createuser = new CreateUser();
		createuser.show(true);
	}
	@SuppressWarnings("deprecation")
	public static void showmainwindow(String loggedas)
	{
		mainwindow = new MainWindow(loggedas);
		mainwindow.show(true);
	}
	@SuppressWarnings("deprecation")
	public static void showaboutbox(String loggedas)
	{
		aboutbox = new AboutBox(loggedas);
		aboutbox.show(true);
	}
	public static void showaddfriendwindow(String loggedas)
	{
		addfriend = new AddFriend(loggedas);
		addfriend.show(true);
		
	}
	public static void closeloginwindow()
	{
		loginwindow.dispose();
	}
	public static void closeusercreatorwindow()
	{
		createuser.dispose();
	}
	public static void closemainwindow()
	{
		mainwindow.dispose();
	}
	public static void closeaboutbox()
	{
		aboutbox.dispose();
	}
	public static void closeaddfriendwindow()
	{
		addfriend.dispose();
	}
}
