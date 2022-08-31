import java.io.*;
import java.util.*;

class BridgeProblem {

public static void main(String [] args) throws IOException {
	
	AlgorithmSolver ProblemSolver1= new AlgorithmSolver();
	
	Algorithm usc= Algorithm.UCS;
	
	String filename="/Users/shaden/Desktop/Untitled.txt";
	
	ArrayList<Set<Integer>> UcsSolve=ProblemSolver1.solve(filename, usc);

      System.out.println(".. Bridge crossing problem solution using Uniform Cost Search ..\n");
      System.out.println("Sequence of the trips with direction for : "+ProblemSolver1.eastSide);
      ProblemSolver1.UcsStrategy.solutionDetailes();
      System.out.println("---------------------------------\nSolution: "+UcsSolve+"\n---------------------------------");
	  System.out.println("Solution Cost: "+ProblemSolver1.UcsStrategy.solutionCost+"\n---------------------------------");
	  System.out.println("Number of nodes generated (Search Cost): "+ProblemSolver1.UcsStrategy.NodesGenerated_Space+"\n---------------------------------");
	  System.out.println("Number of nodes kept in memory (Space Requirement): "+ProblemSolver1.UcsStrategy.NodesGenerated_Space+"\n---------------------------------");
    
	  
	AlgorithmSolver ProblemSolver2= new AlgorithmSolver();
	
	Algorithm a= Algorithm.AStar;
	
	ArrayList<Set<Integer>> AstarSolve=ProblemSolver2.solve(filename, a);
	
	  System.out.println("\n\n.. Bridge crossing problem solution using Astar search ..\n");
      System.out.println("Sequence of the trips with direction for : "+ProblemSolver2.eastSide);
      ProblemSolver2.AstarStrategy.solutionDetailes();
      System.out.println("---------------------------------\nSolution: "+AstarSolve+"\n---------------------------------");
	  System.out.println("Solution Cost: "+ProblemSolver2.AstarStrategy.solutionCost+"\n---------------------------------");
	  System.out.println("Number of nodes generated (Search Cost): "+ProblemSolver2.AstarStrategy.NodesGenerated_Space+"\n---------------------------------");
	  System.out.println("Number of nodes kept in memory (Space Requirement): "+ProblemSolver2.AstarStrategy.NodesGenerated_Space+"\n---------------------------------");
	  
   }//end main  

}//end class

//-------------------------------

enum Algorithm { UCS, AStar; }

//-------------------------------

interface Solver { ArrayList<Set<Integer>> solve(String fileName, Algorithm alg); }

//-------------------------------

class AlgorithmSolver implements Solver {
	  
	  UniformCostSearch UcsStrategy;
	  AStar AstarStrategy;
	  ArrayList<Integer>  eastSide=new ArrayList<Integer>();
	
	  public ArrayList<Set<Integer>> solve(String fileName, Algorithm alg) {
		  
		  ArrayList<Integer> westSide=new ArrayList<Integer>();

			try {
				
				Scanner read=new Scanner(new File(fileName));
				read.nextInt(); 
			 
				while(read.hasNextInt())   
				eastSide.add(read.nextInt());   } catch (Exception e) { System.err.println(e); }
			
			
		  state InitialState=new state(eastSide,westSide,'E');
		  state goalState=new state(westSide,eastSide,'W');
		  		  
		  switch (alg) {
		  
		  case UCS:  node Initial1= new node(null, 0, InitialState);
			  
			         UcsStrategy=new UniformCostSearch(Initial1,goalState);
		
	                 return UcsStrategy.UcsSolution();
	                 

		  case AStar: Anode Initial2= new Anode(null, 0, InitialState);
			  
			          AstarStrategy=new AStar(Initial2,goalState);
			          
		              return AstarStrategy.AstarSolution();

		  default: return null; }  }//end slove
	  
}//end AlgorithmSolver class

//-------------------------------

class state {
	
	ArrayList<Integer> eastSide;
    ArrayList<Integer> westSide;
	char flash;
	int stepCost;
	int countNumberOfNodes;
	
	public state(ArrayList<Integer> eastSide,ArrayList<Integer> westSide, char flash) {
		this.eastSide = eastSide;
		this.westSide = westSide;
		this.flash = flash; }
	
	//-------------------------------

	public state(ArrayList<Integer> eastSide, ArrayList<Integer> westSide, int stepCost,char flash) {
		this.eastSide = eastSide;
		this.westSide = westSide;
		this.stepCost = stepCost; 
		this.flash=flash; }
	
	//-------------------------------

	public ArrayList<state> expand(){ 
		
	ArrayList<state> successors=new ArrayList<state>();
	
	if(!eastSide.isEmpty()&&westSide.isEmpty()) {/*case initial state*/
    
	    ArrayList<ArrayList<Integer>> s=FindCombinations(eastSide,2);/*find all combinations of two persons will be move to the west*/
	    
	    for(int l=0;l<s.size();l++) {
		    	
			ArrayList<Integer> e=new ArrayList<Integer>();
			ArrayList<Integer> w=new ArrayList<Integer>();
		    int max =Collections.max(s.get(l));/*step cost*/
		    w=s.get(l);/*add the two persons will be move, to the west side of new state*/
		       
		    for(int j=0;j<eastSide.size();j++) {
		    if(!w.contains(eastSide.get(j)))
		    e.add(eastSide.get(j));/*add the element that do not move to west side, to the east side of new state*/ }
		        
		    successors.add(new state(e,w,max,'W')); countNumberOfNodes++; }  }//end if


	
	else if(!eastSide.isEmpty()&&!westSide.isEmpty()&&flash=='W') {/*case movement from west to east*/
	
	    for(int j=0;j<westSide.size();j++) {/*one person will be return, so take all possible persons than can return*/ 
	    
	    ArrayList<Integer> e=new ArrayList<Integer>();
	    ArrayList<Integer> w=new ArrayList<Integer>();
		 
	    for(int a=0;a<eastSide.size();a++) 
			e.add(eastSide.get(a));/*add all the element in east side to the east side of new state*/
		    
			e.add(westSide.get(j));/*one person will be return from west side,so add it here*/  
			
		    successors.add(new state(e,w,westSide.get(j),'E')); countNumberOfNodes++;  }
	   
	    for(int i=0;i<successors.size();i++) {
		    for(int j=0;j<westSide.size();j++) {
		    if(!successors.get(i).eastSide.contains(westSide.get(j)))
		    successors.get(i).westSide.add(westSide.get(j));/*add the rest of persons in the west side*/ }   }  }//end if
		
		
	
	else if(!eastSide.isEmpty()&&!westSide.isEmpty()&&flash=='E') {/*case movement from east to west*/
		
	    ArrayList<ArrayList<Integer>> s=FindCombinations(eastSide,2);
	
		for(int l=0;l<s.size();l++) {
		    	
		    ArrayList<Integer> e=new ArrayList<Integer>();
		    ArrayList<Integer> w=new ArrayList<Integer>();
		    
		    for(int j=0;j<westSide.size();j++) 
	        w.add(westSide.get(j));/*add all the element in west side to the west side of new state*/
		    
	        int max=Collections.max(s.get(l));/*step cost*/ 
	    
		    for(int j=0;j<s.get(l).size();j++) 
		    w.add(s.get(l).get(j));/*add the two persons will move to the west side*/ 
	      
	        for(int j=0;j<eastSide.size();j++) {
	        if(!w.contains(eastSide.get(j)))
	        e.add(eastSide.get(j));/*add the rest of persons in the east side*/ }
			        
			successors.add(new state(e,w,max,'W')); countNumberOfNodes++; }  }//end if
		
			return successors; }//end expand
		
	//-------------------------------
		
	public ArrayList<ArrayList<Integer>>  FindCombinations(ArrayList<Integer >input,int r) { 
 
	  ArrayList<ArrayList<Integer>> subsets = new ArrayList<>();

      int[] s=new int[r];                 
                                
      if (r<=input.size()) {

    	  for (int i=0; (s[i]= i)<r-1; i++);  
          subsets.add(getSubset(input, s));
          
          for(;;) {
        	  
	          int i;
	
	          for (i=r-1;i>= 0&&s[i]==input.size()-r+i;i--); 
	          
	          if (i<0) 
	          break; 
	          
	          s[i]++;  
	          
	          for (++i;i<r;i++)   
	          s[i]=s[i-1]+1;  
	          
	          subsets.add(getSubset(input,s));  }    }//end if
	      
     return subsets; }
	
	//-------------------------------
	
	ArrayList<Integer> getSubset(ArrayList<Integer > input, int[] subset) {
		
		ArrayList<Integer> result=new ArrayList<Integer>(subset.length); 
		
          for (int i=0;i<subset.length;i++) 
          result.add(i ,input.get(subset[i]));
          
          return result;  }

}//end state class

//-------------------------------

class node implements Comparable<node> {/*Uniform Cost Search node*/
	
	 int pathCost;
	 state state;
	 node parent; 

	public node(node parent, int pathCost, state state) {
		this.parent=parent;
		this.state=state;
		this.pathCost=pathCost; }
	
	//-------------------------------

   public int compareTo(node n){/*to order the nodes with their path cost in frontier*/
  	
  	if(this.pathCost==n.pathCost)
       return 0;
  	  
  	else if(pathCost>n.pathCost)
       return 1;
   
      else return -1; }
  
}//end node class

//-------------------------------

class UniformCostSearch {
	
    state goal;
    node Initial;
    PriorityQueue<node> frontier;
    ArrayList<state> explored;
    int NodesGenerated_Space;
    int solutionCost;
    node lastNode;
	
	public UniformCostSearch (node Initial,state goal) {
		this.Initial=Initial;
		frontier=new PriorityQueue<node>(); 
		explored=new ArrayList<state>();
		this.goal=goal; }
	
	//-------------------------------
	
	public ArrayList<Set<Integer>> UcsSolution() {
		
	    frontier.add(Initial); 	
	 
	    while(!frontier.isEmpty()) {
	    
		    node test=frontier.remove();
		    
		    if(test.state.westSide.containsAll(goal.westSide)){	
		        lastNode=test;
			    solutionCost=test.pathCost;
			    NodesGenerated_Space++;/*count initial node*/
			    return solution(test); }
		   
		    explored.add(test.state);
		
		    ArrayList<state> successors=test.state.expand();
		    NodesGenerated_Space+=test.state.countNumberOfNodes;
		    
		    for(int i=0;i<successors.size();i++) {
		    	
			    node newNode=new node(test,test.pathCost+successors.get(i).stepCost,successors.get(i));
			    
			    boolean flag=false;
			    
				Iterator<node> iterator = frontier.iterator();
				
				while(iterator.hasNext()) {/*if there is an equal state with higher g(n),replace it with new node*/
					node n=iterator.next();
					if(newNode.state.equals(n.state)&&newNode.pathCost<n.pathCost) {
					frontier.remove();
			        frontier.add(newNode);
			        flag=true; }  }
			
			    if(!flag) {
				    if(!frontier.contains(newNode)||!explored.contains(newNode.state))
				    frontier.add(newNode); }   }//end for
		    
		      }//end while
		    
			return null;
			
	     }//end UcsSolution 

	 //-------------------------------
	 
	 public  ArrayList<Set<Integer>> solution(node node){
		 
		 ArrayList<Set<Integer>> solution=new ArrayList<Set<Integer>>();
    	 
		 while(node.parent!=null) {
			 
		     if(node.state.flash=='W') {
					 
				 Set<Integer> set=new HashSet<>();
				 
				 for(int i=0;i<node.state.westSide.size();i++) {	 
					 if( !node.parent.state.westSide.contains(node.state.westSide.get(i))) 
					 set.add(node.state.westSide.get(i));  }
					
			 solution.add(set);  }
		     
		     if(node.state.flash=='E') {
				 
				 Set<Integer> set=new HashSet<>();
				 
				 for(int i=0;i<node.state.eastSide.size();i++) { 
					 if(!node.parent.state.eastSide.contains(node.state.eastSide.get(i))) 
				     set.add(node.state.eastSide.get(i));  }
				
		     solution.add(set);  }
				 	 
		     node=node.parent;  }//end while
			
         Collections.reverse(solution);/*i search for the solution started with the last action taken, so reverse the solution to start with the first action*/
         
	     return solution;  }//end solution 

	 //-------------------------------
	 
     public  void solutionDetailes(){
		 
		 ArrayList<Set<Integer>> sol=solution(lastNode);
		 
		 for(int i=0;i<sol.size();i++) {
			 if(i%2==0)
			 System.out.println((i+1)+". "+sol.get(i)+" move to the west side");	
			 
			 else System.out.println((i+1)+". "+sol.get(i)+" return to the east side");	 }
		 
		 }//end solution 
	 
	 
}//end UniformCostSearch class

//-------------------------------

class Anode implements Comparable<Anode> {/*AStar node*/
	
	 int pathCost;
	 int f;/*evaluation function of Astar=> pathCost+heuristic*/
	 state state;
	 Anode parent; 
	 int heuristic;

	public Anode(Anode parent, int pathCost, state state) {
		this.parent=parent;
		this.state=state;
		this.pathCost=pathCost; }

  public int compareTo(Anode n){/*to order the nodes with their pathCost+heuristic value in frontier*/
	    	
	   	 if(this.f==n.f)
	   		return 0;
	   	  
	   	 else if(f>n.f)
	     return 1;
	    
	     else return -1; }

}//end Anode class

//-------------------------------

class AStar{
	
	state goal;
	Anode Initial;
    PriorityQueue<Anode> frontier;
    ArrayList<state> explored;
    int NodesGenerated_Space;
    int solutionCost;
    Anode lastNode;
	
	public AStar(Anode Initial,state goal) {
		this.Initial=Initial;
		frontier=new PriorityQueue<Anode>(); 
		explored=new ArrayList<state>();
		this.goal=goal; }
	
	public ArrayList<Set<Integer>> AstarSolution() {

	int sum=0;
	
	for(int i=0;i<Initial.state.eastSide.size();i++) 
	sum+=Initial.state.eastSide.get(i);/*sum of speeds in eastSide list*/
	    
	Initial.heuristic=sum/2;/*divide it by two to guarantee there is no overestimation, the result will be floored by default*/
	
	Initial.f=sum/2;/*since step cost in intial state is zero the f(n) will be same as heuristic(n)*/
	
    frontier.add(Initial); 	

    while(!frontier.isEmpty()) {
  
    Anode test=frontier.remove();

	   if(test.state.westSide.containsAll(goal.westSide)) {/*check goal state*/
		   lastNode=test;
		   solutionCost=test.pathCost; 
		   NodesGenerated_Space++;/*to count the root*/
		   return solution(test); }
	   
	   explored.add(test.state);
	
	   ArrayList<state> s=test.state.expand();
	   
	   NodesGenerated_Space+=test.state.countNumberOfNodes;
	    
	   for(int i=0;i<s.size();i++) {
	    
		   int Successorheuristic=Math.abs(test.heuristic-s.get(i).stepCost)+1;/*heurstic function must not be a negative*/

		   Anode newNode=new Anode(test,test.pathCost+s.get(i).stepCost,s.get(i));
		    
		   if(newNode.state.westSide.containsAll(goal.westSide)) { 
			   newNode.heuristic=0;/*the goal heuristic must be zero*/
			   newNode.f=newNode.pathCost; }
		    
		   else { newNode.heuristic=Successorheuristic;
		          newNode.f=Successorheuristic+newNode.pathCost;/*calculate f(n) of AStar*/ }

		   boolean flag=false;
		    
		   Iterator<Anode> iterator = frontier.iterator();
			
			while(iterator.hasNext()) {/*if there is an equal state with higher f(n),replace it with new node*/
				Anode n=iterator.next();
				if(newNode.state.equals(n.state)&&newNode.f<n.f) {
				frontier.remove();
		        frontier.add(newNode);
		        flag=true; }  }
		
		    if(!flag) {
		    if(!frontier.contains(newNode)||!explored.contains(newNode.state))
		    frontier.add(newNode); }  }//end for
		    
	    }//end while
	
		return null;  }//end AstarSolution
	
	//-------------------------------

	 public  ArrayList<Set<Integer>> solution(Anode node){
		 
		 ArrayList<Set<Integer>> solution=new ArrayList<Set<Integer>>();

		 while(node.parent!=null) {
			 
		     if(node.state.flash=='W') {
					 
			 Set<Integer> set=new HashSet<>();
			 
			 for(int i=0;i<node.state.westSide.size();i++) { 
				 if( !node.parent.state.westSide.contains(node.state.westSide.get(i))) 
				 set.add(node.state.westSide.get(i));  }
			
			 solution.add(set);  }
		     
		     if(node.state.flash=='E') {
				 
			 Set<Integer> set=new HashSet<>();
			 
			 for(int i=0;i<node.state.eastSide.size();i++) {		 
				 if(!node.parent.state.eastSide.contains(node.state.eastSide.get(i))) 
			     set.add(node.state.eastSide.get(i));  }
			
			 solution.add(set);  }
				 	 
		     node=node.parent;  }//end while
		
         Collections.reverse(solution);

	     return solution;  }//end solution 
	 
	 //------------------------------
	 
	 public  void solutionDetailes(){
		 
		 ArrayList<Set<Integer>> sol=solution(lastNode);
		 
		 for(int i=0;i<sol.size();i++) {
			 if(i%2==0)
			 System.out.println((i+1)+". "+sol.get(i)+" move to the west side");	
			 
			 else System.out.println((i+1)+". "+sol.get(i)+" return to the east side");	 }  }//end solutionDetailes

	 
}//end AStar class