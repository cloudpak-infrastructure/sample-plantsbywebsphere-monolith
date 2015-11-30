package org.pbw.test;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Unit test for simple App.
 */
public class InitialTest {

	static String wasHome = "";
	static String appToInstall = "";

	@BeforeClass
	public static void setUpTest() throws IOException {
		wasHome = System.getProperty("was.test.home");
		appToInstall = System.getProperty("app.location");

		File outLog = new File(wasHome, "logs/server1/SystemOut.log");

		BufferedReader br = new BufferedReader(new FileReader(outLog));
		while (br.readLine() != null) {
		}
		File app = new File(appToInstall);

		File wasMonitoredDir = new File(wasHome, "monitoredDeployableApps/servers/server1");
		FileUtils.copyFileToDirectory(app, wasMonitoredDir);

		String line = br.readLine();
		while (true) {
			if (line == null) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			} else if (line.contains("CWLDD0008I")) {
				break;
			}

			line = br.readLine();
		}

		br.close();
	}

	/**
	 */
	@Test
	public void testStartPage() {
		WebClient webClient = new WebClient();
		webClient.getOptions().setJavaScriptEnabled(false);
		HtmlPage page;
		try {
			page = webClient.getPage("http://localhost:9080/PlantsByWebSphere/");
			assertEquals("Plants By WebSphere Promo", page.getTitleText());

		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			webClient.close();
		}

	}
	
	@Test
	public void testImageReturned() {
		WebClient webClient = new WebClient();
		webClient.getOptions().setJavaScriptEnabled(false);
		Page page;
		try {
			page = webClient.getPage("http://localhost:9080/PlantsByWebSphere/javax.faces.resource/veggies_strawberries_48.jpg.jsf?ln=images");
			assertEquals("The image should return OK", 200, page.getWebResponse().getStatusCode());

		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			webClient.close();
		}

	}
	
}
