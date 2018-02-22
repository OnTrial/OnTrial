package org.developer.wwb.core.utils;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * long类型的id结构是64位的二进制，构成如下： 0（long符号位）-0000000000 0000000000 0000000000
 * 0000000000 0 (41位时间戳) --- 00000(生成器组id) ---00000（生成器id） ---0000000000
 * 00（12位一毫秒内自增序列） 这样的好处是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞（由生成器组id
 * 和生成器id作区分），并且效率较高
 */
public class IdGenerator {
	//
	private final static long twepoch = 1404741526058L;// 起始时间戳，为代码编写时间
	private final static long idBits = 5L;// 生成器ID位数
	private final static long groupIdBits = 5L;// 生成器组ID位数
	private final static long sequenceBits = 12L;// 毫秒内自增位
	private final static long maxId = -1L ^ (-1L << idBits);// 生成器ID最大值
	private final static long maxGroupId = -1L ^ (-1L << groupIdBits);// 生成器组ID最大值
	private final static long sequenceMask = -1L ^ (-1L << sequenceBits);// 自增ID掩码
	private final static long idShift = sequenceBits;// ID左移12位
	private final static long groupIdShift = sequenceBits + idBits;// 集群ID左移17位
	private final static long timestampLeftShift = sequenceBits
			+ idBits + groupIdBits;// 时间毫秒左移22位

	private static long lastTimestamp = -1L;
	private ReentrantLock lock = new ReentrantLock();
	private long sequence = 0L;
	private final long databaseId;
	private final long dbGroupId;
	/**
	 * 为了避免并发阻塞，每个线程一个实例
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static ThreadLocal<IdGenerator> defaultIdGenerator=new ThreadLocal();
	
	public static IdGenerator  getDefault(){
		if(defaultIdGenerator.get()==null){
			defaultIdGenerator.set(new IdGenerator(Thread.currentThread().getId()%maxGroupId,Thread.currentThread().getId()%maxId));
		}
		return defaultIdGenerator.get();
	}
	
	public IdGenerator(long groupId, long id) {
		if (id > maxId || id < 0) {
			throw new IllegalArgumentException("Id不能大于%d或小于0");
		}
		if (groupId > maxGroupId || groupId < 0) {
			throw new IllegalArgumentException("集群Id不能大于%d或小于0");
		}
		this.databaseId = id;
		this.dbGroupId = groupId;
	}

	public long nextId() {
		lock.lock();
		try {
			long timestamp = timeGen();
			if (timestamp < lastTimestamp) {
				throw new RuntimeException("系统时间倒退 "
						+ (lastTimestamp - timestamp) + "毫秒，不允许生成id。");
			}
			if (lastTimestamp == timestamp) {
				// 当前毫秒内，则+1
				sequence = (sequence + 1) & sequenceMask;
				if (sequence == 0) {
					// 当前毫秒内计数满了，则等待下一毫秒
					timestamp = toNextMillis(lastTimestamp);
				}
			} else {
				sequence = 0;
			}
			lastTimestamp = timestamp;
			// ID偏移组合生成最终的ID，并返回ID
			long nextId = ((timestamp - twepoch) << timestampLeftShift)
					| (dbGroupId << groupIdShift)
					| (databaseId << idShift) | sequence;
			return nextId;
		} finally {
			lock.unlock();
		}
	}

	private long toNextMillis(final long lastTimestamp) {
		long timestamp = this.timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = this.timeGen();
		}
		return timestamp;
	}

	private long timeGen() {
		return System.currentTimeMillis();
	}
}
