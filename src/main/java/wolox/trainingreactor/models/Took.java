package wolox.trainingreactor.models;

import lombok.Data;

@Data
public class Took {

    private String name;
    private Integer length;


    public Took(String name, Integer length) {
        this.name = name;
        this.length = length;
    }
}
