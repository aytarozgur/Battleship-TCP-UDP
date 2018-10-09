package battleship.Network;

public class Message <T> implements java.io.Serializable{

	private eMessageType messageType;
	private T dataContainer;

	public eMessageType getMessageType() {
		return this.messageType;
	}

	/**
	 * 
	 * @param messageType
	 */
	public void setMessageType(eMessageType messageType) {
		this.messageType = messageType;
	}

	public T getDataContainer() {
		return this.dataContainer;
	}

	/**
	 * 
	 * @param dataContainer
	 */
	public void setDataContainer(T dataContainer) {
		this.dataContainer = dataContainer;
	}

}