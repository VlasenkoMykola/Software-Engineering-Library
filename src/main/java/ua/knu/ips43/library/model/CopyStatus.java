package ua.knu.ips43.library.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CopyStatus {
    free("free"),
    booked("booked"),
    given("given"),
    lost("lost");

    private final String name;

    //private CopyStatus(String label) {
    //    this.name = label;
    //}
    public static CopyStatus valueOfLabel(String label) {
	for (CopyStatus e : values()) {
	    if (e.name.equals(label)) {
		return e;
	    }
	}
	return null;
    }
}
