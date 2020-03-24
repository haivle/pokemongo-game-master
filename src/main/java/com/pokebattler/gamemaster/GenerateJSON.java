package com.pokebattler.gamemaster;

import POGOProtos.Data.*;
import POGOProtos.Networking.Responses.*;
import com.google.protobuf.util.*;

import java.io.*;

public class GenerateJSON {
	public GenerateJSON() {
	}

	public void writeJSON(InputStream is, OutputStream os) throws IOException {
		GameMasterDecoder response = GameMasterDecoder.parseFrom(is);
		//old mode used here....
		//*
		DownloadItemTemplatesResponse.Builder old_mode =  DownloadItemTemplatesResponse.newBuilder();
		old_mode.setTimestampMs(response.getBatchId());

		for (GameMasterDecoder.ClientGameMasterTemplate t : response.getTemplateList()) {
			old_mode.addItemTemplate(t.getData());
		}

		old_mode.build();
		JsonFormat.Printer printer = JsonFormat.printer();
		try (OutputStreamWriter writer = new OutputStreamWriter(os)) {
			printer.appendTo(old_mode, writer);
			System.out.println();
			System.out.println("-------------------------------------------------------------------------------");
			System.out.println("Generated templates:");
			System.out.println("	Decoded templates: " + old_mode.getItemTemplateCount());
			System.out.println("	TimestampMs      : " + old_mode.getTimestampMs());
			System.out.println("-------------------------------------------------------------------------------");
			System.out.println();
		}
		//*/

		//new mode used in app....
		//for uses this out file comment up and remove comment here.
		/*
		JsonFormat.Printer printer = JsonFormat.printer();
		try (OutputStreamWriter writer = new OutputStreamWriter(os)) {
			printer.appendTo(response, writer);
			System.out.println();
			System.out.println("-------------------------------------------------------------------------------");
			System.out.println("Generated templates:");
			System.out.println("	Decoded templates: " + response.getTemplateList().size());
			System.out.println("	Deleted templates: " + response.getDeletedTemplateList().size());
			System.out.println("	Experiment ids   : " + response.getExperimentIdList().size());
			System.out.println("	BatchId          : " + response.getBatchId());
			System.out.println("-------------------------------------------------------------------------------");
			System.out.println();
		}
		//*/
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

		GenerateJSON gen = new GenerateJSON();
		try (OutputStream os = args.length == 2 ? new FileOutputStream(new File(args[1])) : System.out;
			 InputStream is = new FileInputStream(f)) {
			gen.writeJSON(is, os);
		}
	}
}
