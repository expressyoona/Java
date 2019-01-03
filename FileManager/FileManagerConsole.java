import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

public class FileManagerConsole {

	Scanner kb;
	String command;
	String currentPath;
	String homePath;
	String rootPath;
	
	public static void throwError(String str) {
		System.out.println("[X] Error: " + str);
	}

	public FileExplorerConsole() throws IOException {
		kb = new Scanner(System.in);
		//For Linux user
		String username = System.getProperty("user.name").toString();
		homePath = new String("/home/" + username);
		rootPath = new String("/");
		currentPath = homePath;
		System.out.println("Welcome back, " + username.substring(0, 1).toUpperCase() + username.substring(1, username.length()));
		while (true) {

			if (currentPath.equals(homePath)) {
				System.out.print("$~ ");
			} else {
				System.out.print("~" + currentPath + "$ ");
			}

			command = kb.nextLine().trim();

			// Handle command
			/*	Function
				1. Change directory
				2. List of directory
				3. Get absolute path of current path
				4. Copy a file to another directory
				5. Move a file to another directory
				6. Deleta a file
				7. Create a new directory
				And upcomming...
				- Read an exist text file
				- Create a new text file/empty file
				- Run file Python, Java, C/C++
				- Edit a simple text file(*.txt, *.java. *.py)
			 */
			if (command.startsWith("cd")) {
				changeDirectory();
			} else if (command.startsWith("ls")) {
				viewFolder();
			} else if (command.equals("pwd")) {
				System.out.println(new File(currentPath).getAbsolutePath());
			} else if (command.equals("exit")) {
				System.out.println("Good bye!");
				System.exit(0);
			} else if (command.startsWith("cp")) {
				copyFile();
			} else if (command.startsWith("mv")) {
				moveFile();
			} else if (command.startsWith("rm")) {
				removeFile();
			} else if (command.startsWith("mkdir")) {
				makeDirectory();
			} else {
				System.out.println("Command " + command + " not found");
			}
		}
	}

	private void makeDirectory() {
		// TODO Auto-generated method stub
		String[] mkdir = command.split(" ");
		if (mkdir.length == 1) {
			throwError("Too few arguments");
		}
		else if (mkdir.length > 2) {
			throwError("Too many arguments");
		}
		else {
			File directory = null;
			if (mkdir[1].startsWith("/home")) {
				directory = new File(mkdir[1]);
			}
			else {
				directory = new File(Paths.get(currentPath, mkdir[1]).toString());
			}
			
			if (directory.exists()) {
				throwError("The directory name is alreay existed");
			}
			else {
				directory.mkdirs();
			}
		}
	}

	private void removeFile() {
		// TODO Auto-generated method stub
		String[] rm = command.split(" ");
		if (rm.length > 2) {
			throwError("Too many arguments");
		}
		else if (rm.length == 1) {
			throwError("Too few arguments");
		}
		else {
			File fi = new File(Paths.get(currentPath, rm[1]).toString());
			if (!fi.exists()) {
				throwError("No such file or directory: " + fi.getAbsolutePath());
			}
			else {
				System.out.print("Are you sure delete file " + fi.getName() + "?[Y/N]");
				String acc = new Scanner(System.in).nextLine();
				if (acc.equalsIgnoreCase("Y")) {
					if (!fi.delete()) {
						throwError("Can not delete file or directory. Try again");
					}
				}
			}
		}
	}

	private void moveFile() throws IOException {
		// TODO Auto-generated method stub
		String[] mv = command.split(" ");
		if (mv.length < 3) {
			throwError("Too few arguments");
		} else if (mv.length > 3) {
			throwError("Too many arguments");
		} else {
			File src = null;
			if (mv[1].startsWith("/home")) {
				src = new File(mv[1]);
			} else {
				src = new File(Paths.get(currentPath, mv[1]).toString());
			}
			if (!src.exists()) {
				throwError("No such file or directory: " + src.getAbsolutePath());
			} else if (src.isDirectory()) {
				throwError(src.getAbsolutePath() + " is directory");
			} else {
				File dst = null;
				if (mv[2].contains("/")) {
					dst = new File(mv[2]);
				} else {
					dst = new File(Paths.get(currentPath, mv[2]).toString());
				}
				
				if (!dst.exists()) {
					throwError("No such file or directory: " + dst.getAbsolutePath());
				} else {
					FileUtils.moveFileToDirectory(src, dst, false);
				}
			}
		}
	}

	private void copyFile() throws IOException {
		// TODO Auto-generated method stub
		String[] cp = command.split(" ");
		if (cp.length < 3) {
			throwError("Too few arguments");
		} else if (cp.length > 3) {
			throwError("Too many arguments");
		} else {
			File src = null;
			if (cp[1].startsWith("/home")) {
				src = new File(cp[1]);
			} else {
				src = new File(Paths.get(currentPath, cp[1]).toString());
			}

			if (!src.exists()) {
				throwError("No such file or directory: " + src.getAbsolutePath());
			} else if (src.isDirectory()) {
				throwError(src.getAbsolutePath() + " is directory");
			} else {
				File dst = null;
				if (cp[2].startsWith("/home")) {
					dst = new File(cp[2]);
				} else {
					dst = new File(Paths.get(currentPath, cp[2]).toString());
				}

				if (!dst.exists()) {
					throwError("No such file or directory: " + dst.getAbsolutePath());
				} else {
					FileUtils.copyFileToDirectory(src, dst);
				}
			}
		}
	}

	private void viewFolder() {
		// TODO Auto-generated method stub
		if (command.equals("ls")) {
			File fi = new File(currentPath);
			String[] files = fi.list();
			Arrays.sort(files);
			for (String file : files) {
				File f = new File(fi.getAbsolutePath() + "/" + file);
				if (!f.isHidden()) {
					if (f.isFile()) {
						System.out.println(file);
					} else if (f.isDirectory()) {
						System.out.println(file);
					}
				}
			}
		} else {
			String ls[] = command.split(" ");
			if (ls.length > 2) {
				throwError("Too many arguments");
			} else {
				File fi = new File(Paths.get(homePath, ls[ls.length - 1]).toString());
				if (!fi.exists()) {
					throwError("No such file or directory: " + fi.getAbsolutePath());
				} else {
					viewFolder(fi.getAbsolutePath());
				}
			}
		}
	}

	private void viewFolder(String path) {
		// TODO Auto-generated method stub
		File fi = new File(path);
		String[] files = fi.list();
		for (String file : files) {
			System.out.println(file);
		}
	}

	private void changeDirectory() {
		// TODO Auto-generated method stub
		if (command.equals("cd")) {
			currentPath = homePath;
		} else {
			String cd[] = command.split(" ");
			if (cd.length > 2) {
				throwError("Too many arguments");
			} else {
				if (cd[cd.length - 1].equals("../")) {
					currentPath = new File(currentPath).getParent();
				} else {
					File fi = null;
					//System.out.println("Current path is: " + currentPath);
					if (!currentPath.startsWith("/home")) {
						fi = new File(cd[cd.length - 1].toString());
					}
					else {
						fi = new File(Paths.get(homePath, cd[cd.length - 1]).toString());
					}
					if (!fi.exists()) {
						throwError("No such file or directory: " + fi.getAbsolutePath());
					} else {
						currentPath = fi.getAbsolutePath();
					}
				}
			}
			

		}
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new FileManagerConsole();
	}


}
