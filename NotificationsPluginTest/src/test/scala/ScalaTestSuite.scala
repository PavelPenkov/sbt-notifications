import org.scalatest.FunSuite

class FirstTest extends FunSuite {
  test("success") {
    assert(true)
  }
  
  test("failure") {
    assert(false)
    assert(true)
  }

  test("another") {
    assert(true)
  }
  
  test("pending")(pending)
  
  ignore("ignored") {
    assert(false)
  }
  
  test("error") {
    val xs = Array(1)
    assert(xs(1) == 1)
  }
}

class SecondTest extends FunSuite {
  test("success") {
    assert(true)
  }
  
  test("failure") {
    assert(false)
  }
  
  test("pending")(pending)
  
  ignore("ignored") {
    assert(false)
  }
  
  test("error") {
    val xs = Array(1)
    assert(xs(1) == 1)
  }
}
