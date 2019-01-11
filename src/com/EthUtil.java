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
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

public class EthUtil {
	private final static Web3j web3 = Web3j.build(new HttpService("https://mainnet.infura.io/f7lCFyNhcZYorDJ17vYD"));
	
	public EthUtil() {
		
	}
	
	public String createWallet(String password, String savePath) throws Exception {
		File saveLocation = new File(savePath);
		if(!saveLocation.exists()) saveLocation.mkdir();
		
		String fileName = WalletUtils.generateNewWalletFile(password, saveLocation);
		Credentials cre = WalletUtils.loadCredentials(password, new File(savePath + fileName));
		String address = cre.getAddress();
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("fileName", fileName);
		result.put("address", address);
		
		JSONObject json = new JSONObject(result);
		return json.toString();
	}
	
	public String send(String fileName, String password, double amount, String toAddress, String savePath) throws Exception {
		Credentials credentials = WalletUtils.loadCredentials(password, savePath + fileName);
		TransactionReceipt t = Transfer.sendFunds(web3, 
				credentials, 
				toAddress, 
	    		BigDecimal.valueOf(amount), 
	    		Unit.ETHER).send();
		return t.getTransactionHash();
	}
	
	public BigDecimal getBalance(String address) throws IOException {
		String bal = web3.ethGetBalance(address, DefaultBlockParameter.valueOf("latest")).send().getBalance() + "";
		return Convert.fromWei(bal, Convert.Unit.ETHER);
	}
	
	public BigInteger getBlock() throws IOException {
		return web3.ethBlockNumber().send().getBlockNumber();
	}
	
	public String getTransaction(String txid) throws IOException {
		JSONObject json = new JSONObject();
		Transaction t = web3.ethGetTransactionByHash(txid).send().getResult();
		json.put("blockHash", t.getBlockHash());
		json.put("blockNumber", t.getBlockNumber());
		json.put("from", t.getFrom());
		json.put("gas", t.getGas());
		json.put("gasPrice", t.getGasPrice());
		json.put("hash", t.getHash());
		json.put("to", t.getTo());
		json.put("value", t.getValue());
		return json.toString();
	}
	
	public String getReceipt(String txid) throws IOException {
		JSONObject json = new JSONObject();
		TransactionReceipt t = web3.ethGetTransactionReceipt(txid).send().getResult();
		json.put("blockHash", t.getBlockHash());
		json.put("blockNumber", t.getBlockNumber());
		json.put("contractAddress", t.getContractAddress());
		json.put("cumulativeGasUsed", t.getCumulativeGasUsed());
		json.put("from", t.getFrom());
		json.put("gasUsed", t.getGasUsed());
		json.put("status", t.getStatus());
		json.put("to", t.getTo());
		json.put("transactionHash", t.getTransactionHash());
		json.put("transactionIndex", t.getTransactionIndex());
		return json.toString();
	}
}
