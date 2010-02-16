package kg.apc.jmeter.threads;

import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.threads.gui.AbstractThreadGroupGui;

public class SteppingThreadGroupGui
      extends AbstractThreadGroupGui
{
   public SteppingThreadGroupGui()
   {
      init();
   }

   public String getLabelResource()
   {
      return this.getClass().getSimpleName();
   }

   @Override
   public String getStaticLabel()
   {
      return "Stepping Thread Group";
   }

   public TestElement createTestElement()
   {
      SteppingThreadGroup tg = new SteppingThreadGroup();
      modifyTestElement(tg);
      return tg;
   }

   public void modifyTestElement(TestElement tg)
   {
      super.configureTestElement(tg);
   }

   @Override
   public void configure(TestElement tg)
   {
      super.configure(tg);
   }
}
