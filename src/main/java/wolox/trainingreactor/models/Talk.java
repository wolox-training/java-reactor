package wolox.trainingreactor.models;

import lombok.Data;

@Data
public class Talk {

    private String name;
    private Integer length;


    public Talk(String name, Integer length) {
        this.name = name;
        this.length = length;
    }
}
