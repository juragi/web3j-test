package com;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

import org.json.JSONObject;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.ClientConnectionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;


public class Main {
	
	public static void main(String[] args) throws Exception {
		String savePath = "/root/eth/keystore/";
		File currentJavaJarFile = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		String currentJavaJarFilePath = currentJavaJarFile.getAbsolutePath();
		savePath = currentJavaJarFilePath.replace(currentJavaJarFile.getName(), "keystore/");
		
		String command = args[0];
		EthUtil ethUtil = new EthUtil();
		
		if(command.equals("create")) {
			if(args.length < 2) {
				System.out.println("type password");
			}else{
				String password = args[1];
				System.out.println(ethUtil.createWallet(password, savePath));
			}
		}else if(command.equals("send")) {
			String fileName = args[1];
			String password = args[2];
			double amount = Double.parseDouble(args[3]);
			String toAddress = args[4];
			try {
				System.out.println(ethUtil.send(fileName, password, amount, toAddress, savePath));
			}catch(ClientConnectionException e) {
				System.out.println(e.getMessage());
			}
			
		}else if(command.equals("balance")) {
			String address = args[1];
			System.out.println(ethUtil.getBalance(address));
		}else if(command.equals("getBlock")) {
			System.out.println(ethUtil.getBlock());
		}else if(command.equals("tx")) {
			String txid = args[1];
			System.out.println(ethUtil.getTransaction(txid));
		}else if(command.equals("receipt")) {
			String txid = args[1];
			System.out.println(ethUtil.getReceipt(txid));
		}
		
	}

}
