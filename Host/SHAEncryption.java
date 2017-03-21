package Host;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHAEncryption {
	
	// taken from http://stackoverflow.com/questions/3103652/hash-string-via-sha-256-in-java
	public static String makeShaHash(String input) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(input.getBytes());
		}
		catch (NoSuchAlgorithmException e) {
			System.err.println("no algorithm for the type of encoding.");
			e.printStackTrace();
		}
		byte[] digest = md.digest();
		
		return convertByteArrayToHex(digest);
	}
	
	// taken from http://www.mkyong.com/java/java-sha-hashing-example/
	// adding extra 0 in hexstring taken from http://stackoverflow.com/questions/3103652/hash-string-via-sha-256-in-java
	private static String convertByteArrayToHex(byte[] hash) {
		StringBuilder hexString = new StringBuilder();
		for (int i = 0; i < hash.length; i++) {
	      String hex = Integer.toHexString(0xFF & hash[i]);
	      if(hex.length() == 1) {
	    	  hexString.append('0');
	      }
	      hexString.append(hex);
	    }
		return hexString.toString();
	}
	
	public static void testEncryption() {
		String toTest = "Hash";
		String preTestedAnswer = "a91069147f9bd9245cdacaef8ead4c3578ed44f179d7eb6bd4690e62ba4658f2";
		
		System.out.println("preTestedAnswer = " + preTestedAnswer);
		System.out.println("convertedAnswer = " + makeShaHash(toTest));
		System.out.println("The hash came out correctly: " + makeShaHash(toTest).equals(preTestedAnswer));
	}
}
