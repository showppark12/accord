package org.neociclo.accord.odetteftp.camel;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.neociclo.odetteftp.TransferMode;
import org.neociclo.odetteftp.protocol.DeliveryNotification;
import org.neociclo.odetteftp.protocol.OdetteFtpObject;
import org.neociclo.odetteftp.service.TcpClient;
import org.neociclo.odetteftp.support.InOutSharedQueueOftpletFactory;
import org.neociclo.odetteftp.support.SessionConfig;

public class OdetteOperations {

	protected final transient Log log = LogFactory.getLog(getClass());
	private OdetteEndpoint endpoint;
	private TcpClient client;
	private Queue<OdetteFtpObject> outgoingQueue = new ConcurrentLinkedQueue<OdetteFtpObject>();
	private Queue<OdetteFtpObject> incomingQueue = new ConcurrentLinkedQueue<OdetteFtpObject>();
	private boolean hasOut;
	private boolean hasIn;
	private InOutSharedQueueOftpletFactory factory;

	public OdetteOperations(OdetteEndpoint odetteEndpoint) {
		this.endpoint = odetteEndpoint;
	}

	public boolean isConnected() {
		if (client != null) {
			return client.isConnected();
		}

		return false;
	}

	/**
	 * <p>
	 * Starts polling files and notifications from Odette server. If Producer
	 * has added any file/notification to be sent, will do during this session.
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 * @throws ClientException
	 */
	public void pollServer() throws Exception {
		if (client == null || !client.isConnected()) {
			initClient();

			final OdetteConfiguration cfg = endpoint.getConfiguration();
			client = new TcpClient(cfg.getHost(), cfg.getPort(), factory);
			client.connect(true);
		}
	}

	private void initClient() {
		final OdetteConfiguration cfg = endpoint.getConfiguration();

		TransferMode identifyTransferMode = identifyTransferMode();

		SessionConfig session = new SessionConfig();
		session.setUserCode(cfg.getOid());
		session.setUserPassword(cfg.getPassword());
		session.setTransferMode(identifyTransferMode);
		session.setDataExchangeBufferSize(cfg.getBufferSize());
		session.setWindowSize(cfg.getWindowSize());

		factory = new InOutSharedQueueOftpletFactory(session, outgoingQueue, null, incomingQueue);
		factory.setEventListener(new InOutOftpletListener(endpoint));
	}

	private TransferMode identifyTransferMode() {
		if (hasIn && hasOut)
			return TransferMode.BOTH;
		else if (hasIn)
			return TransferMode.RECEIVER_ONLY;
		else if (hasOut)
			return TransferMode.SENDER_ONLY;

		return null;
	}

	public void offer(DeliveryNotification notif) {
		outgoingQueue.offer(notif);
	}

	public void disconnect() {
		if (client != null && client.isConnected()) {
			try {
				client.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setHasInQueue() {
		hasIn = true;
	}

	public void setHasOutQueue() {
		hasOut = true;
	}

}