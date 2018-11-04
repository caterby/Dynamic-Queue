/*Jie Liu Netid:jxl164830 */
import java.util.LinkedList;
public class Queueing {
	public static double Seed=1111.0;
	public static double uni_rv(){
		double k=16807.0;
		double m=2.147483647e9;
		double rv;
		Seed=(k*Seed)%m;
		rv=Seed/m;
		return rv;
	}
	public static double exp_rv(double lambda){
		double exp;
		exp=((-1)/lambda)*java.lang.Math.log(uni_rv());
		return exp;
	}
	
    public static void adding(LinkedList<Event> eventlinklist, Event newevent){
    	if(eventlinklist.size()>0){
    		int i = 0;
    		Event current;
    		while(i < eventlinklist.size()){
    			current =eventlinklist.get(i);
    			if(current.time > newevent.time)
    			{
    				eventlinklist.add(i, newevent);
    				break;
    			}
    			i++;
    		}
    		if(i >= eventlinklist.size())
    			eventlinklist.add(i, newevent);
    		else
    			;
    	}
      else if(eventlinklist.size()==0){
    			eventlinklist.addFirst(newevent);
      }
    }
	public static void simulation(double lambda, double mu1, double mu2){
		LinkedList<Event> Eventlinklist=new LinkedList<Event>();
		int ARR=0;
		int DEPL1=1;
		int DEPL2=3;
		int DEPH1=2;
		int DEPH2=4;
		double currentclock=0.0;
		double baseclock=0.0;
		int LowN1=0;                                     // # of current jobs in system
		int LowN2=0;
		int HighN1=0;
		int HighN2=0;
		int LowDepN1=0;                                     // # of current jobs in system
		int LowDepN2=0;
		int HighDepN1=0;
		int HighDepN2=0;
		int DepN=0;
		int LowtotalN1=0;                                  // total jobs
		int LowtotalN2=0;
		int HightotalN1=0;
		int HightotalN2=0;                              // block jobs
		double ELowN1=0.0;                               //expected value of the number of jobs in system
		double ELowN2=0.0;
		double EHighN1=0.0;
		double EHighN2= 0.0;
		double PH=0.25;
		double PL=0.75;
		double r11L = 0.2;
		double r12L=0.8;
		double r2d = 0.5;
		double r21=0.5;
		//use 1 indicate high priority, use 2 indicate low priority
		Event CurrentEvent;
		int iterator=0;
		if(uni_rv()<PL){
			adding(Eventlinklist, new Event(exp_rv(lambda),ARR,2)); 
		}
		else{
			adding(Eventlinklist, new Event(exp_rv(lambda),ARR,1)); 
		}
		while(iterator<500000)                                //iteration 500000 times
		{
			baseclock=currentclock;                             // store clock value
			CurrentEvent=Eventlinklist.removeFirst();                 //get next Event from list
			currentclock=CurrentEvent.time;                  //update clock value
			switch(CurrentEvent.type){
			case 0://Arr
				ELowN1+=LowN1*(currentclock-baseclock);
				ELowN2+=LowN2*(currentclock-baseclock);
				EHighN1+=HighN1*(currentclock-baseclock);
				EHighN2+=HighN2*(currentclock-baseclock);
				if(uni_rv()<PL){
					LowN1++;
					LowtotalN1++;
					if(LowN1==1){
						adding(Eventlinklist, new Event(currentclock+exp_rv(mu1),DEPL1,2));
					}
					/*
					if(LowN1==1&&HighN1==0){
						adding(Eventlinklist, new Event(currentclock+exp_rv(mu1),DEPL1,2));
					}
					*/
				}
				else{
					HighN2++;
					HightotalN2++;
					if(HighN2==1){
						adding(Eventlinklist, new Event(currentclock+exp_rv(mu2),DEPH2,1));
					}
				}
				
				if(uni_rv()<PL){
					adding(Eventlinklist, new Event(currentclock+exp_rv(lambda),ARR,2)); 
				}
				else{
					adding(Eventlinklist, new Event(currentclock+exp_rv(lambda),ARR,1)); 
				}
				break;
			case 1: //DEPL1
				ELowN1+=LowN1*(currentclock-baseclock);
				LowN1--;
				LowDepN1++;
				//iterator++;
				ELowN2+=LowN2*(currentclock-baseclock);
				EHighN1+=HighN1*(currentclock-baseclock);
				EHighN2+=HighN2*(currentclock-baseclock);
				/*
				if(LowN1>0&&HighN1==0){
					adding(Eventlinklist, new Event(currentclock+exp_rv(mu1),DEPL1,2));
				}
				*/
				if(LowN1>0){
					adding(Eventlinklist, new Event(currentclock+exp_rv(mu1),DEPL1,2));
				}
				if(uni_rv()<r11L){
					LowN1++;
					LowtotalN1++;
					if(LowN1==1){
						adding(Eventlinklist, new Event(currentclock+exp_rv(mu1),DEPL1,2));
					}
				}
				else{
					LowN2++;
					LowtotalN2++;
					if(LowN2==1){
						adding(Eventlinklist, new Event(currentclock+exp_rv(mu2),DEPL2,2));
					}
				}
				break;
			case 2://DEPH1
				ELowN1+=LowN1*(currentclock-baseclock);
				ELowN2+=LowN2*(currentclock-baseclock);
				EHighN1+=HighN1*(currentclock-baseclock);
				HighN1--;
				HighDepN1++;
				//iterator++;
				EHighN2+=HighN2*(currentclock-baseclock);
				if(HighN1>0){
					adding(Eventlinklist, new Event(currentclock+exp_rv(mu1),DEPH1,1));
				}
				HighN2++;
				HightotalN2++;
				if(HighN2==1){
					adding(Eventlinklist, new Event(currentclock+exp_rv(mu2),DEPH2,1));
				}
				break;
			case 3://DEPL2
				ELowN1+=LowN1*(currentclock-baseclock);
				ELowN2+=LowN2*(currentclock-baseclock);
				LowN2--;
				LowDepN2++;
				//iterator++;
				EHighN1+=HighN1*(currentclock-baseclock);
				EHighN2+=HighN2*(currentclock-baseclock);
				if(LowN2>0){
					adding(Eventlinklist, new Event(currentclock+exp_rv(mu2),DEPL2,2));
				}
				if(uni_rv()<r21){
					LowN1++;
					LowtotalN1++;
					if(LowN1==1){
						adding(Eventlinklist, new Event(currentclock+exp_rv(mu1),DEPL1,2));
					}
				}
				else{
				    DepN++;
					iterator++;
				}
				break;
			case 4://DEPH2
				ELowN1+=LowN1*(currentclock-baseclock);
				ELowN2+=LowN2*(currentclock-baseclock);
				EHighN1+=HighN1*(currentclock-baseclock);
				EHighN2+=HighN2*(currentclock-baseclock);
				HighN2--;
				HighDepN2++;
				iterator++;
				if(HighN2>0){
					adding(Eventlinklist, new Event(currentclock+exp_rv(mu2),DEPH2,1));
				}
				if(uni_rv()<r21){
					HighN1++;
					HightotalN1++;
					if(HighN1==1){
						adding(Eventlinklist, new Event(currentclock+exp_rv(mu1),DEPH1,1));
					}
				}
				else{
					DepN++;
					iterator++;
				}
				break;
				
		}
	}
		double ET_Low1 = ELowN1/LowtotalN1;
		double ET_High1 = EHighN1/HightotalN1; 
		ELowN1=ELowN1/currentclock;
		ELowN2=ELowN2/currentclock;
		EHighN1=EHighN1/currentclock;
		EHighN2=EHighN2/currentclock;
		//question1
		double ThLow1=LowDepN1/currentclock;
		double ThLow2=LowDepN2/currentclock;
		double ThHigh1=HighDepN1/currentclock;
		double ThHigh2=HighDepN2/currentclock;
		//question2 ELowN1, ELowN2, EHighN1, EHighN2
		//question3 ET_Low1, ET_High1
		//double ET_Low1 = ELowN1/LowtotalN1;
		//double ET_High1 = EHighN1/HightotalN1;                                   // get utilization
	System.out.println("ThLow1:"+ThLow1);
	System.out.println("ThLow2:"+ThLow2);
	System.out.println("ThHigh1:"+ThHigh1);
	System.out.println("ThHigh2:"+ThHigh2);
	//question 2
	System.out.println("E[Low1N]:"+ELowN1);
	System.out.println("E[Low2N]:"+ELowN2);
	System.out.println("E[High1N]:"+EHighN1);
	System.out.println("E[High2N]:"+EHighN2);
	//question 3
	System.out.println("ET_Low1:"+ET_Low1);
	System.out.println("ET_High1:"+ET_High1);
	System.out.println(" ");
}
	public static void main(String[] args){
		
		double mu1=25;
		double mu2=40;
		for(int i=1;i<=10;i++){
			System.out.println("for case lambda = "+i);
			System.out.println("The result is as follows:");
			System.out.println(" ");
			simulation(i,mu1, mu2);
			
		}
	}
}
