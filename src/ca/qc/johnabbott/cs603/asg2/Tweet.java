package ca.qc.johnabbott.cs603.asg2;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Tweet {

	public static final DateFormat dateFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
	
	private long tweetId, userId;
	private String user, name, tweet;
	private Date created;
	private int retweetCount;

	/**
	 * Creates an immutable tweet object 
	 * @param tweetId
	 * @param userId
	 * @param user
	 * @param name
	 * @param tweet
	 * @param created
	 * @param retweetCount
	 */
	public Tweet(long tweetId, long userId, String user, String name, String tweet, Date created, int retweetCount) {
		super();
		this.tweetId = tweetId;
		this.userId = userId;
		this.user = user;
		this.name = name;
		this.tweet = tweet;
		this.created = created;
		this.retweetCount = retweetCount;
	}

	/**
	 * Creates an immutable tweet object - parses "created" field into a Date object
	 * @param tweetId
	 * @param userId
	 * @param user
	 * @param name
	 * @param tweet
	 * @param created
	 * @param retweetCount
	 * @throws ParseException
	 */
	public Tweet(long tweetId, long userId, String user, String name, String tweet, String created, int retweetCount) throws ParseException {
		this(tweetId, userId, user, name, tweet, dateFormatter.parse(created), retweetCount);
	}
	
	/**
	 * Creates an immutable tweet object - parses "created" field into a Date object.
	 * Provides a default value for retweet count.
	 * @param tweetId
	 * @param userId
	 * @param user
	 * @param name
	 * @param tweet
	 * @param created
	 * @throws ParseException
	 */
	public Tweet(long tweetId, long userId, String user, String name, String tweet, String created) throws ParseException {
		this(tweetId, userId, user, name, tweet, dateFormatter.parse(created), -1);
	}
	
	/**
	 * Creates an immutable tweet object 
	 * Provides a default value for retweet count.
	 * @param tweetId
	 * @param userId
	 * @param user
	 * @param name
	 * @param tweet
	 * @param created
	 */
	public Tweet(long tweetId, long userId, String user, String name, String tweet, Date created) {
		this(tweetId, userId, user, name, tweet, created, -1);
	}
	
	/**
	 * Get tweet ID.
	 * @return tweet ID
	 */
	public long getTweetId() {
		return tweetId;
	}
	
	/**
	 * Get user's ID.
	 * @return user ID
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * Get user's name (ex: @xyz)
	 * @return user name
	 */
	public String getUser() {
		return user;
	}
	
	/**
	 * Get user's "real" name
	 * @return user name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get tweet message.
	 * @return tweet message
	 */
	public String getTweet() {
		return tweet;
	}

	/**
	 * Get creation date.
	 * @return creation date
	 */
	public Date getCreated() {
		return created;
	}
	
	/**
	 * Get retweet count. Warning: currently not working...
	 * @return retweet count
	 */
	public int getRetweetCount() {
		return retweetCount;
	}

	/**
	 * Get creation date as a String.
	 * @return creation date
	 */
	public String getCreatedAsString() {
		return dateFormatter.format(created);
	}
	
	/**
	 * Get String representation of tweet.
	 * @return representation of tweet
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(user);
		sb.append(" at " + dateFormatter.format(created));
		sb.append("\n");
		sb.append(tweet);
		return sb.toString();
	}
	
}
