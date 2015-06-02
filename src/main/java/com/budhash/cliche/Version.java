package com.budhash.cliche;

public class Version {
	private static final String VERSION;
	static {
		String versionID = null;
		try {
			versionID = Version.class.getPackage().getImplementationVersion();
			Version.class.getPackage().getImplementationVersion();
		} catch (Exception ex) {
			// do nothing
		}

		VERSION = (null == versionID) ? "unknown" : versionID;
	}

	private Version() {

	}

	public static String getVersion() {
		return VERSION;
	}

	public static void main(String[] args) {
		System.out.println(getVersion());
	}
}
