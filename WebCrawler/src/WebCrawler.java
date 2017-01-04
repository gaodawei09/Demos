import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;

public class WebCrawler {
	ArrayList<String> allurlSet = new ArrayList<String>();// ���е���ҳurl����Ҫ����Ч��ȥ�ؿ��Կ���HashSet
	ArrayList<String> notCrawlurlSet = new ArrayList<String>();// δ��������ҳurl
	HashMap<String, Integer> depth = new HashMap<String, Integer>();// ������ҳ��url���
	int crawDepth = 3; // �������
	int threadCount = 10; // �߳�����
	int count = 0; // ��ʾ�ж��ٸ��̴߳���wait״̬
	public static final Object signal = new Object(); // �̼߳�ͨ�ű���

	public static void main(String[] args) {
		final WebCrawler wc = new WebCrawler();
		wc.addUrl("http://weather.news.sina.com.cn/", 2);
		long start = System.currentTimeMillis();
		System.out.println("��ʼ����.........................................");
		wc.begin();

		while (true) {
			if (wc.notCrawlurlSet.isEmpty() && Thread.activeCount() == 1 || wc.count == wc.threadCount) {
				long end = System.currentTimeMillis();
				System.out.println("�ܹ�����" + wc.allurlSet.size() + "����ҳ");
				System.out.println("�ܹ���ʱ" + (end - start) / 1000 + "��");
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

	// ����ҳsUrl
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