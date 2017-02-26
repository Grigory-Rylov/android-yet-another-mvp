# yet-another-mvvm-lib
Yet another MVVM library for android

The Library helps forget about android view-life cycle and write program with mvvm-like approach.
Even if process was killed by system, after resore - your view and presenters holds their last state,
without manual storing state to Bundle (I tell you a secret: library do it automatic)

View has single public method - onUpdateState() - it received events from presenter.
Events implements MvpState interface - that is marker interface, inherited from Serializable.
Your State-models must implements MvpState and its fields must implements Serializable.