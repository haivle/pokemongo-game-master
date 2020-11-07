package com.pokebattler.gamemaster;

import POGOProtos.Rpc.*;
import com.google.protobuf.util.*;

import java.io.*;

public class GenerateAssetDigestJSON {
	public GenerateAssetDigestJSON() {
	}

	public void writeJSON(InputStream is, OutputStream os) throws IOException {
		AssetDigestOutProto.Builder response = AssetDigestOutProto.parseFrom(is).toBuilder();
		response.setResult(AssetDigestOutProto.Result.SUCCESS);
		JsonFormat.Printer printer = JsonFormat.printer();
		try (OutputStreamWriter writer = new OutputStreamWriter(os)) {
			printer.appendTo(response, writer);
			System.out.println();
			System.out.println("-------------------------------------------------------------------------------");
			System.out.println("Generated digests:");
			System.out.println("	Decoded digests: " + response.getDigestCount());
			System.out.println("	TimestampMs    : " + response.getTimestamp());
			System.out.println("	Result         : " + response.getResult());
			System.out.println("-------------------------------------------------------------------------------");
			System.out.println();
		}
	}

	public static void main(String[] args) throws Exception {
		if (args.length == 0 || args.length > 2) {
			System.err.println("USAGE: java -jar pokemongo-game-master-2.46.0.jar BINARY_INPUT_FILE [optional JSON_OUTPUT_FILE]");
			return;
		}

		File f = new File(args[0]);
		if (!f.exists()) {
			System.err.println("File not found: " + args[0]);
			return;
		}

		GenerateAssetDigestJSON gen = new GenerateAssetDigestJSON();
		try (OutputStream os = args.length == 2 ? new FileOutputStream(new File(args[1])) : System.out;
			 InputStream is = new FileInputStream(f)) {
			gen.writeJSON(is, os);
		}
	}
}
