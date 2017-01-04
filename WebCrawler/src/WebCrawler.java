import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;

public class WebCrawler {
	ArrayList<String> allurlSet = new ArrayList<String>();// 所有的网页url，需要更高效的去重可以考虑HashSet
	ArrayList<String> notCrawlurlSet = new ArrayList<String>();// 未爬过的网页url
	HashMap<String, Integer> depth = new HashMap<String, Integer>();// 所有网页的url深度
	int crawDepth = 3; // 爬虫深度
	int threadCount = 10; // 线程数量
	int count = 0; // 表示有多少个线程处于wait状态
	public static final Object signal = new Object(); // 线程间通信变量

	public static void main(String[] args) {
		final WebCrawler wc = new WebCrawler();
		wc.addUrl("http://weather.news.sina.com.cn/", 2);
		long start = System.currentTimeMillis();
		System.out.println("开始爬虫.........................................");
		wc.begin();

		while (true) {
			if (wc.notCrawlurlSet.isEmpty() && Thread.activeCount() == 1 || wc.count == wc.threadCount) {
				long end = System.currentTimeMillis();
				System.out.println("总共爬了" + wc.allurlSet.size() + "个网页");
				System.out.println("总共耗时" + (end - start) / 1000 + "秒");
				System.exit(1);
			}

		}
	}

	private void begin() {
		for (int i = 0; i < threadCount; i++) {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						String tmp = getAUrl();
						if (tmp != null) {
							crawler(tmp);
						} else {
							synchronized (signal) {
								try {
									count++;
									signal.wait();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}

						}
					}
				}
			}, "thread-" + i).start();
		}
	}

	public synchronized String getAUrl() {
		if (notCrawlurlSet.isEmpty())
			return null;
		String tmpAUrl;
		tmpAUrl = notCrawlurlSet.get(0);
		notCrawlurlSet.remove(0);
		return tmpAUrl;
	}

	public synchronized void addUrl(String url, int d) {
		notCrawlurlSet.add(url);
		allurlSet.add(url);
		depth.put(url, d);
	}

	// 爬网页sUrl
	public void crawler(String sUrl) {
		URL url;
		try {
			url = new URL(sUrl);
			URLConnection urlconnection = url.openConnection();
			urlconnection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
			InputStream is = url.openStream();
			BufferedReader bReader = new BufferedReader(new InputStreamReader(is));
			StringBuffer sb = new StringBuffer();// 
			String rLine = null;
			while ((rLine = bReader.readLine()) != null) {
				sb.append(rLine);
				sb.append("/r/n");
			}

			int d = depth.get(sUrl);
			if (d < crawDepth) {
				parseContext(sb.toString(), d + 1);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void parseContext(String context, int dep) {
		String regex = "<a href.*?/a>";
		Pattern pt = Pattern.compile(regex);
		Matcher mt = pt.matcher(context);
		while (mt.find()) {
			Matcher myurl = Pattern.compile(".png?").matcher(mt.group());
			while (myurl.find()) {
				String str = myurl.group().replaceAll("src=\"|\"", "");
				if (str.contains("png")) { 
					if (!allurlSet.contains(str)) {
						System.out.println(str);
						addUrl(str, dep);
						if (count > 0) { 
							synchronized (signal) { 
								count--;
								signal.notify();
							}
						}

					}
				}
			}
		}
	}
}