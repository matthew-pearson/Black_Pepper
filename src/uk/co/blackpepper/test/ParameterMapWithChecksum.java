package uk.co.blackpepper.test;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;

public class ParameterMapWithChecksum {

	private final Map<String, String[]> parameterMap;
	private final int checkSum;
	
	public ParameterMapWithChecksum(Map<String, String[]> parameterMap) {
		this.parameterMap = parameterMap;
		checkSum = betterHashCode(parameterMap);
	}

	/**
	 * Adapted from code in AbstractMap to cope with arrays.
	 * @param parameterMap
	 * @return hashcode
	 */
    public static int betterHashCode(Map<String, String[]> parameterMap) {
        int h = 0;
        for (Entry<String, String[]> entry : parameterMap.entrySet()) {
        	int keyHash = entry.getKey().hashCode();
        	int valueHash = Arrays.hashCode(entry.getValue());
        	int entryHash = keyHash ^ valueHash;
        	h += entryHash;
        }
        return h;
    }
	
	public Map<String, String[]> getParameterMap() {
		return parameterMap;
	}

	public int getCheckSum() {
		return checkSum;
	}

	public String toJson() {
		return new Gson().toJson(this);
	}
	
	@Override
	public String toString() {
		return toJson();
	}

	@Override
	public int hashCode() {
		return checkSum;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParameterMapWithChecksum other = (ParameterMapWithChecksum) obj;
		if (parameterMap == null) {
			if (other.parameterMap != null)
				return false;
		} else if (!parameterMap.equals(other.parameterMap))
			return false;
		return true;
	}
	
}
