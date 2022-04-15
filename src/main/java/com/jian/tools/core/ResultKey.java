package com.jian.tools.core;

public class ResultKey {

	/** 错误码 */
	public final static String CODE = "code";
	/** 错误信息 */
	public final static String MSG = "msg";
	/** 错误信息转译。 如：多语言 */
	public final static String TMSG = "tmsg"; //translate msg
	/** 不宜直接展示给用户的信息。 如：异常、调试 */
	public final static String NMSG = "nmsg"; //none msg or no show no display or debug msg
	/** 数据 */
	public final static String DATA = "data";
	/** 总条数 */
	public final static String TOTAL = "total";
	/** 页码 */
	public final static String PAGE = "page";
	/** 每页条数 */
	public final static String ROWS = "rows";
	
}
