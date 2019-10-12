package Requests;

public class FillRequest {

    private String[] pathParams;
    private int generations;

    public FillRequest(String[] pathParams, int generations) {
        this.pathParams = pathParams;
        this.generations = generations;
    }

    public void setPathParams(String[] pathParams) {
        this.pathParams = pathParams;
    }

    public void setGenerations(int generations) {
        this.generations = generations;
    }

    public String[] getPathParams() {
        return pathParams;
    }

    public int getGenerations() {
        return generations;
    }

}
