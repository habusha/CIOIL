package gov.taxes.infra.github.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.MDC;

import gov.taxes.infra.github.enums.SeverityEnum;
import gov.taxes.infra.github.exception.ValidationError;
import gov.taxes.infra.github.mapping.TaxesMapperImpl;

/**
 * 
 * @author Dan Erez
 *
 *         The main context class, passed on every requests, per thread.
 *         Regarding global id, the rule is: If we didn't have a global id,
 *         generate one. Otherwise, use existing global id.
 */
public class TaxesContext {

	private static final ThreadLocal<TaxesContext> threadLocal = new ThreadLocal<>();

	public static final String APP_NAME = "APP_NAME";
	public static final String GLOBAL_ID = "GLOBAL_ID";	
	public static final String USER_NAME = "USER_NAME";
	public static final String MF_PASSWORD = "MF_USER_PASSWORD";
	public static final String USER_CONTEXT = "USER_CONTEXT";
	public static final String USER_ROLE = "USER_ROLE";
	public static final String USER_IP = "USER_IP";
	public static final String DEFAULT_USER = "משתמש";
	public static final String UNKNOWN = "לא ידוע"; 
	// Paging and Sorting
	public static final String PAGE_NO = "page";
	public static final String SORT_FIELD = "sortField";
	public static final String ROWS_PER_PAGE = "rowsPerPage"; 
	public static final String IS_ASCENDING = "isAscending";
 
	private String globalId;
	// This is the logged in user name
	private String userName, userRole, mfUserName, mfPassword;
	private String ip;
	// This map is for application usage and isn't used by Tashtit
	private Map<String, Object> map;
	// Paginng info
	//
	private Integer totalRecCount;
	//
	private List<ValidationError> infoMessages;	

	private TaxesContext() {
		initContext();
	}

	private void initContext() {
		map = new HashMap<String, Object>();
		infoMessages = new ArrayList<>();
		MDC.put(APP_NAME, TaxesMapperImpl.APP_NAME);
		MDC.remove(GLOBAL_ID);
		setGlobalId(UUID.randomUUID().toString());
		userName = UNKNOWN; 
		userRole = UNKNOWN;
		mfPassword = null;
		mfUserName = null;
		ip = null;
		totalRecCount = null;
	}

	/**
	 * Return the context from the Thread Local.
	 * 
	 * @return SMContext
	 */
	public static TaxesContext getContext() {
		TaxesContext result = null;
		result = threadLocal.get();
		if (result == null) {
			result = new TaxesContext();
			threadLocal.set(result);
		}
		return result;
	}

	/**
	 * Since thread might be recycled, there's no guarantee that the old context
	 * will be removed from the (recycled) thredlocal. Call this in the entrance of
	 * any channel.
	 */
	public void clean() {
		initContext();
	}

	public String getGlobalId() {
		return globalId;
	}

	public void setGlobalId(String globalId) {
		this.globalId = globalId;
		MDC.put(GLOBAL_ID, globalId);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Object get(String key) {
		return map.get(key);
	}

	public void put(String key, Object val) {
		map.put(key, val);
	}

	public Map<String, Object> getMap() {
		return map;
	}

	/**
	 * @return
	 */
	public Integer getTotalRecCount() {
		return totalRecCount;
	}

	/**
	 * @param totalRecCount
	 */
	public void setTotalRecCount(Integer totalRecCount) {
		this.totalRecCount = totalRecCount;
	}

	public List<ValidationError> getInfoMessages() {
		return infoMessages;
	}

	public void setInfoMessages(List<ValidationError> infoMessages) {
		this.infoMessages = infoMessages;
	}

	public static void addInfoMessage(String field, String msg) {

		ValidationError ve = new ValidationError(field, msg, SeverityEnum.INFORMATION);
		TaxesContext.getContext().getInfoMessages().add(ve);

	}

	public String getEncryptedUserContext() {	
		if (this.mfUserName == null || this.mfPassword == null) {
			return null;
		}
		return this.mfUserName+","+this.mfPassword;
	}

	public void setEncryptedUserContext(String userContext) {
		if (userContext != null) {
			String[] info = userContext.split(",");
			this.mfUserName = info[0];
			this.mfPassword = info[1];
		}
	}

	public String getMFPassword() {
		return mfPassword;
	}

	public String getMFUserName() {		
		return mfUserName;
	}

	public void setMFUser(String username) {
		mfUserName = username;
	}
	
	public void setMFPassword(String pwd) {
		mfPassword = pwd;
	}

}