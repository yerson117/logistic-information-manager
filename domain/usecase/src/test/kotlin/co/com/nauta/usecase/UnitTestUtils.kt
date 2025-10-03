package co.com.nauta.usecase

import org.mockito.ArgumentCaptor
import org.mockito.Mockito

class UnitTestUtils {

    object MockitoHelper {
        fun <T> anyObject(): T {
            Mockito.any<T>()
            return uninitialized()
        }

        fun <T> anyObject(clazz: Class<T>): T {
            Mockito.any(clazz)
            return uninitialized()
        }

        fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()

        @Suppress("UNCHECKED_CAST")
        private fun <T> uninitialized(): T = null as T
    }
}
