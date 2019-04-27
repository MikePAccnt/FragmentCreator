# Fragment Creator

Tool to auto create an android Fragment class from a provided xml. Currently very simple and requires manually importing everything and setting the correct package name.

Releases contains a version of the jar that you can add to Android Studio as a plugin. First selection is for the destination of the class and second text box is what name you want for the class.

* Only generates kotlin classes
* Only generates variables for views with an id
* Requires the following extension function to be in your project

```kotlin
fun <T: View> Fragment.findViewById(@IdRes id: Int): Lazy<T> {
	return lazy {
		requireView().findViewById<T>(id)
	}
}
```

## Example

### Command: `java -jar fragment-creator.jar fragment_something.xml "SomeFragment"` 

### Input (`fragment_something.xml`)
```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:id="@+id/fragment_container"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

    <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:id="@+id/text_view_1"/>

    <LinearLayout
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:id="@+id/layout_one">

        <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:id="@+id/text_view_2"/>
        <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:id="@+id/text_view_3"/>
        <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:id="@+id/text_view_4"/>

    </LinearLayout>

</FrameLayout>
```

### Output
```kotlin
class SomeFragment : Fragment() {

	private val text_view_1: TextView by findViewById(R.id.text_view_1)
	private val layout_one: LinearLayout by findViewById(R.id.layout_one)
	private val text_view_2: TextView by findViewById(R.id.text_view_2)
	private val text_view_3: TextView by findViewById(R.id.text_view_3)
	private val text_view_4: TextView by findViewById(R.id.text_view_4)

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_testing, container, false)
	}

}
```
