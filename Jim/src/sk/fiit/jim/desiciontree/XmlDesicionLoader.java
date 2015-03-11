package sk.fiit.jim.desiciontree;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.lang.reflect.Method;

import sk.fiit.jim.agent.skills.HighSkill;
import sk.fiit.robocup.library.geometry.Angles;


public class XmlDesicionLoader {
	public Document doc;
	public HighSkill hs;
	
	public XmlDesicionLoader(String fileName, HighSkill hs){
		if(fileName.isEmpty())
			hs = null;
		else
		{
		doc = XmlLoader.Load(fileName + ".xml");
		this.hs = hs;
		}
	}

	public String GetLowSkill(){
		Node dt = doc.getDocumentElement().getFirstChild().getNextSibling().getFirstChild();
		String result = "";
		long startTime = System.nanoTime();
		while(result.equals("") && dt != null){
			if(System.nanoTime() - startTime > 1000000)
				break;
			if(dt.getNodeType() != Node.ELEMENT_NODE){
				dt = dt.getNextSibling();
				continue;
			}
			String cond = GetAttr(dt, "condition");
			if(!cond.equals("") && !cond.equals("else")){
				double valuea = (Double)GetVariable("valuea", dt);
				double valueb = (Double)GetVariable("valueb", dt);
				
				if(!((Element)dt).getAttribute("signb").equals(""))
					valueb = -valueb;
				if(!((Element)dt).getAttribute("signa").equals(""))
					valuea = -valuea;
				
				switch(cond){
					case "lowerThen":
						if(valuea < valueb){
							Node fch = dt.getFirstChild();
							while(fch != null && fch.getNodeType() != Node.ELEMENT_NODE){
								fch = fch.getNextSibling();
							}
							if(!HasChildElements((Element)dt)){
								return dt.getTextContent().replaceAll("\\s+","");
							}
							dt = fch;
							
							continue;
						}
						else{
							dt = dt.getNextSibling();
							while(dt != null && dt.getNodeType() != Node.ELEMENT_NODE){
								dt = dt.getNextSibling();
							}
							continue;
						}
					case "biggerThen":
						if(valuea > valueb){
							Node fch = dt.getFirstChild();
							while(fch != null && fch.getNodeType() != Node.ELEMENT_NODE){
								fch = fch.getNextSibling();
							}
							if(!HasChildElements((Element)dt)){
								return dt.getTextContent().replaceAll("\\s+","");
							}
							dt = fch;
							continue;
						}
						else{
							dt = dt.getNextSibling();
							while(dt != null && dt.getNodeType() != Node.ELEMENT_NODE){
								dt = dt.getNextSibling();
							}
							continue;
						}

				}
			}
			if(cond.equals("else")){
				Node fch = dt.getFirstChild();
				while(fch != null && fch.getNodeType() != Node.ELEMENT_NODE){
					fch = fch.getNextSibling();
				}
				if(!HasChildElements((Element)dt)){
					
					return dt.getTextContent().replaceAll("\\s+","");
				}
				dt = fch;
				continue;
			}
			String func = GetAttr(dt, "function");
			
			if(cond.equals("") && !func.equals("")){
				String params = GetAttr(dt, "params");
				String type = GetAttr(dt, "type");
				if((Boolean)ExecuteFunction(func, params, type)){
					return dt.getTextContent().replaceAll("\\s+","");
				}
				else{
					dt = dt.getNextSibling();
					while(dt != null && dt.getNodeType() != Node.ELEMENT_NODE){
						dt = dt.getNextSibling();
					}
					continue;
				}				
			}
		}
		return "rollback";
	}
	
	public static boolean HasChildElements(Element el) {
	    NodeList children = el.getChildNodes();
	    for (int i = 0;i < children.getLength();i++) {
	        if (children.item(i).getNodeType() == Node.ELEMENT_NODE){
	            return true;
	        }
	    }
	    return false;
	}

	private String GetAttr(Node dt, String Attr){
		return ((Element)dt).getAttribute(Attr);
	}
	
	private Object ExecuteFunction(String func, String params, String type){
		Method method = null;
		String[] parameters = null;
		if (params != null) parameters = params.split(",");
		Object Arg1 = null;
		Object Arg2 = null;
		
		try {
			if(parameters == null){
				method = hs.getClass().getMethod(func);
			}
			else{
			 if(parameters.length == 1){
				 Arg1 = GetVariable(parameters[0], null);
				 method = hs.getClass().getMethod(func, Class.forName(type));
				 
			 }
			 if(parameters.length == 2){
				 Arg1 = GetVariable(parameters[0], null);
				 Arg2 = GetVariable(parameters[1], null);
				 method = hs.getClass().getMethod(func, Class.forName(type), Class.forName(type));
			 }
			}
			if (parameters == null)
				return method.invoke(hs);
			else{
			if(parameters.length == 1)
				return method.invoke(hs,Arg1);
			if(parameters.length == 2)
				return method.invoke(hs,Arg1, Arg2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	private Object GetVariable(String v, Node dt){
		String vName;
		if(dt != null) vName = ((Element)dt).getAttribute(v);
		else vName = v;
		
		if(Defined(vName)){
			NodeList para = doc.getElementsByTagName(vName);
			if(GetAttr(para.item(0), "type").equals("sk.fiit.robocup.library.geometry.Angles")){
				return new Angles(Double.parseDouble(GetAttr(para.item(0), "startAngle")), Double.parseDouble(para.item(0).getTextContent()));
			}
			else
				return Double.parseDouble(para.item(0).getTextContent());
		}
		else {
			if(v.substring(v.length() - 1).equals("b") && GetAttr(dt, "typeb").equals("const")){
				return Double.parseDouble(vName);
			}
			if(v.substring(v.length() - 1).equals("a") && GetAttr(dt, "typea").equals("const")){
				return Double.parseDouble(vName);
			}
			return ExecuteFunction("get" + vName.toUpperCase(), null, null);
		}
	}

	private boolean Defined(String vName) {
		NodeList para = doc.getElementsByTagName(vName);
		return (para != null && para.item(0) != null);
	}
}
