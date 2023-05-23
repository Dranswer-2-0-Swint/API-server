package com.t3q.dranswer.common.util;

import java.util.zip.CRC32;

public class HashUtil {

	public static String makeCRC32(int seq) {
		String seq_str = Integer.toString(seq);
		
		CRC32 crc32 = new CRC32();
		crc32.update(seq_str.getBytes());
		
		long crcValue = crc32.getValue();
		
		return Long.toHexString(crcValue);
	}

}
