package chapter6;

public class GameEvent 
{
	public enum Event
	{
		LOGIC_REQUIRED, REMOVE_ENTITY, ADD_ENTITY, NOTIFY_LOSS, NOTIFY_WIN
	}
	
	private Entity entity;
	private Event event;
	
	private String message = "No message";
	
	public GameEvent(Entity entity, Event e)
	{
		this.entity = entity;
		event = e;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public void setMessage(String message)
	{
		this.message = message;
	}

	public Event getEvent() 
	{
		return event;
	}

	public Entity getEntity() {
		return entity;
	}

}
