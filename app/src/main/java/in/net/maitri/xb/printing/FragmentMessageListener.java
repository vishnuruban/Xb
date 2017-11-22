package in.net.maitri.xb.printing;

public interface FragmentMessageListener {
    void onAppSignal(int iAppSignal);

    void onAppSignal(int iAppSignal, String data);

    void onAppSignal(int iAppSignal, boolean data);

    void onAppSignal(int iAppSignal, byte[] data);

}
