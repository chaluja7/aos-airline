package cz.cvut.aos.airline.entity;

/**
 * @author jakubchalupa
 * @since 31.10.16
 */
public enum StateChoices {

    NEW,
    PAID,
    CANCELLED;

    public static StateChoices fromStringCode(String code) {
        if(code != null) {
            for(StateChoices stateChoices : StateChoices.values()) {
                if(code.equalsIgnoreCase(stateChoices.name())) {
                    return stateChoices;
                }
            }
        }

        return null;
    }

}
