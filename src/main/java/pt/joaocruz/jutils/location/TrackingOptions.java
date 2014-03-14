package pt.joaocruz.jutils.location;

/**
 * Created by BEWARE S.A. on 12/03/14.
 */
public class TrackingOptions {
    public static long DEFAULT_FREQUENCY = 10000;
    public static float DEFAULT_RADIUS = 50;
    public static JProvider DEFAULT_PROVIDER = JProvider.BOTH;



    public static enum JProvider {GPS, NETWORK, BOTH};

    private long frequency;
    private float radius;
    private JProvider provider;

    public TrackingOptions(long frequency, float radius, JProvider provider) {
        this.frequency = frequency;
        this.radius = radius;
        this.provider = provider;
    }

    public TrackingOptions() {
        setFrequency(DEFAULT_FREQUENCY);
        setRadius(DEFAULT_RADIUS);
        setProvider(DEFAULT_PROVIDER);
    }

    public long getFrequency() {
        return frequency;
    }

    public void setFrequency(long frequency) {
        this.frequency = frequency;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public JProvider getProvider() {
        return provider;
    }

    public void setProvider(JProvider provider) {
        this.provider = provider;
    }
}
