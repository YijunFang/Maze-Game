package GUI;

public interface Screen {
    /**
     * Loads the screen 
     */
    public void load();
    /**
     * Updates any dynamic graphical elements on the screen
     */
    public void update();
    /**
     * Make the screen visible
     */
    public void show();
    /**
     * Set the screen's master container.
     */
    public void setMaster(Screen s);
    
}
