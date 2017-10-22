import org.specs2._

class SpecsSuite extends org.specs2.mutable.Specification {
  "Notifications" >> {
    "work for passed" >> {
      true must_== true
    }

    "work for failed" >> {
      true must_== false
    }
  }
}