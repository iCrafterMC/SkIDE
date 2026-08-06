package netscape.javascript

open class JSObject {

    private var delegate: Any? = null

    constructor()

    @Suppress("UNCHECKED_CAST")
    constructor(delegate: Any) {
        this.delegate = delegate
    }

    open fun setMember(key: String, value: Any?) {
        val d = delegate ?: return
        try {
            d::class.java.getMethod("setMember", String::class.java, Any::class.java).invoke(d, key, value)
        } catch (e: Exception) {
            throw JSException("Failed to set member '$key'", e)
        }
    }

    open fun getMember(key: String): Any {
        val d = delegate ?: throw JSException("No delegate to get member '$key'")
        return try {
            d::class.java.getMethod("getMember", String::class.java).invoke(d, key)
        } catch (e: Exception) {
            throw JSException("Failed to get member '$key'", e)
        }
    }

    open fun call(methodName: String, vararg args: Any?): Any {
        val d = delegate ?: throw JSException("No delegate to call method '$methodName'")
        val argTypes = args.map { it?.javaClass ?: Any::class.java }.toTypedArray()
        return try {
            d::class.java.getMethod("call", String::class.java, *argTypes).invoke(d, methodName, *args)
        } catch (e: NoSuchMethodException) {
            d::class.java.getMethod("call", String::class.java, Array<Any>::class.java).invoke(d, methodName, args)
        } catch (e: Exception) {
            throw JSException("Failed to call method '$methodName'", e)
        }
    }

    open fun setSlot(index: Int, value: Any) {
        val d = delegate ?: return
        try {
            d::class.java.getMethod("setSlot", Int::class.java, Any::class.java).invoke(d, index, value)
        } catch (e: Exception) {
            throw JSException("Failed to set slot at index $index", e)
        }
    }

    open fun getSlot(index: Int): Any {
        val d = delegate ?: throw JSException("No delegate to get slot at index $index")
        return try {
            d::class.java.getMethod("getSlot", Int::class.java).invoke(d, index)
        } catch (e: Exception) {
            throw JSException("Failed to get slot at index $index", e)
        }
    }

    open fun eval(script: String): Any {
        val d = delegate ?: throw JSException("No delegate to eval '$script'")
        return try {
            d::class.java.getMethod("eval", String::class.java).invoke(d, script)
        } catch (e: Exception) {
            throw JSException("Failed to eval '$script'", e)
        }
    }

    open fun removeMember(key: String) {
        val d = delegate ?: return
        try {
            d::class.java.getMethod("removeMember", String::class.java).invoke(d, key)
        } catch (e: Exception) {
            throw JSException("Failed to remove member '$key'", e)
        }
    }

    override fun toString(): String = delegate?.toString() ?: "null"
}

class JSException : RuntimeException {
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(message: String) : super(message)
}

fun Any?.asJSObject(): JSObject {
    if (this == null) return JSObject()
    if (this is JSObject) return this
    return JSObject(this)
}
