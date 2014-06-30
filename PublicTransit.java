import java.io.*;
import java.util.*;

public class PublicTransit {
    Scanner sc;
    String[] tokens;
	HashMap<Integer, TreeMap<Integer, ArrayList<Stop>>> routes = new HashMap<Integer, TreeMap<Integer, ArrayList<Stop>>>();
	// Holds the information from the routes.txt file
	HashMap<String, Route> route = new HashMap<String, Route>();
	// Holds the information from the trips.txt file
	HashMap<Integer, Route> trip = new HashMap<Integer, Route>();
	// Holds the information from the stops.txt file
	HashMap<Integer, String> stops = new HashMap<Integer, String>();
	
	PriorityQueue<Leg> queue = new PriorityQueue<Leg>();
	TreeSet<Integer> visited = new TreeSet<Integer>();
	TreeSet<Integer> queuevisited = new TreeSet<Integer>();
	
	File routestxt, tripstxt, stopstxt, stop_timetxt;
	public class Route 
	{
		Route (String id, String name) { this.id = id; this.name = name; }
	
		String name;
		String id;
	}
	
	public class Trip
	{
		Trip (Route r, int id) { route_name = r; this.id = id; }
		
		int id;
		Route route_name;
	}
	
	public class Stop
	{
		Stop(int sid, int time, int tid) {stop_id = sid; this.time = time; trip_id = tid; }
		
		int stop_id;
		int trip_id;
		int time;
	}
	
	@SuppressWarnings("rawtypes")
	public class Leg implements Comparable
	{
		Leg(Stop s, Leg sp) { 
			this_stop = s; 
			prev_stop = sp;
		}
		
		Stop this_stop;
		Leg prev_stop;
		
		
		@Override
		public int compareTo(Object o) { 
			Leg l = (Leg) o;

			return this.this_stop.time - l.this_stop.time;
		}
	}
	
	public static void main(String[] args) {
		new PublicTransit().run();
	}
	
	// Method for opening the routes.txt file
	public void openroutestxt()
	{
		try {
			routestxt = new File("routes.txt");
			sc = new Scanner(routestxt);
			getRoute(route);
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println(routestxt + " file was not found.");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	// Method for opening the trips.txt file
	public void opentripstxt()
	{
		try 
		{
			tripstxt = new File("trips.txt");
			sc = new Scanner(tripstxt);
			
			getTrip(route);
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println(tripstxt +" file was not found.");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	// Method for opening the stops.txt file
	public void openstopstxt()
	{
		try 
		{
			stopstxt = new File("stops.txt");
			sc = new Scanner(stopstxt);
			getStop(stops);

		} 
		catch (FileNotFoundException e) 
		{
			System.out.println(stopstxt+ " file was not found.");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	// Method for opening the stop_times.txt file
	public void openstop_timetxt()
	{
		try 
		{
			stop_timetxt = new File("stop_times.txt");
			sc = new Scanner(stop_timetxt);

			getStopId();			
			sc.close();

		} 
		catch (FileNotFoundException e) 
		{
			System.out.println(stop_timetxt +" file was not found.");
			e.printStackTrace();
			System.exit(0);
		}
		
		sc = new Scanner(System.in);
	}
	
	public void run()
	{
		openroutestxt();
		opentripstxt();
		openstopstxt();
		openstop_timetxt();
		Algorithm();
	}
	// Now comes GetSet method to get 
        //all the needed variables from the input file.
	public void getRoute(HashMap<String, Route> route)
	{
		String sLine;
		
		sLine = sc.nextLine();
		while(sc.hasNext())
		{
			sLine = sc.nextLine();
			String[] extractNames = sLine.split(",");
						
			Route r = new Route(extractNames[2], extractNames[0]);
						
			if(route.containsKey(extractNames[0]) == false)
				route.put(extractNames[0], r);
		}
	}
	
	public void getTrip(HashMap<String,Route> route)
	{
		String s;
		s = sc.nextLine();
		
		while(sc.hasNext())
		{
			s = sc.nextLine();
			tokens = s.split(",");
						
			Route r = route.get(tokens[0]);
				
			trip.put(Integer.parseInt(tokens[2]), r);
		}
	}
	
	public void getStop(HashMap<Integer, String> stops)
	{
		String s;
		s = sc.nextLine();
		
		while(sc.hasNext())
		{
			s = sc.nextLine();
			tokens = s.split(",");
						
			stops.put(Integer.parseInt(tokens[0]), tokens[1]);
		}
	}
	
	public void getStopId()
	{
		String s;
		int endTime = -1, endStop = -1, endTrip = -1,
			currTime, currStop, currTrip;
		
		s = sc.nextLine();
		
		while(sc.hasNext())
		{
			s = sc.nextLine();
			tokens = s.split(",");
			String[] extractTime = tokens[2].split(":");
			String time = extractTime[0] + "" + extractTime[1] + "" + extractTime[2];
			currTime = Integer.parseInt(time);
			currStop = Integer.parseInt(tokens[3]);
			currTrip = Integer.parseInt(tokens[0]);
						
			if(currTrip == endTrip)
			{
				if(routes.containsKey(endStop) == false)
					routes.put(endStop, new TreeMap<Integer, ArrayList<Stop>>());
								
				if(routes.get(endStop).containsKey(endTime) == false)
					routes.get(endStop).put(endTime, new ArrayList<Stop>());
							
				routes.get(endStop).get(endTime).add(new Stop(currStop, currTime, currTrip));
			}
						
			endStop = currStop;
			endTime = currTime;
			endTrip = currTrip;	
		}
	}
	
	/**
	 * Dijkstra algorithm finds the shortest path
         * given a start and stop id and start time and it has been implemented
         * in next function.
	 * start_stop_id: the starting stop for the trip inputed by user
	 * end_stop_id: the ending stop for the trip inputed by user
	 * depart_time: the time that you left for the trip inputed by user
	 */
	public void Algorithm()
	{
		int start_stop_id, end_stop_id,	depart_hr, depart_min, time = 0;
	
		// get starting stop_id
		start_stop_id = sc.nextInt();
		// get ending stop_id
		end_stop_id = sc.nextInt();
		// get departure hours
		depart_hr = sc.nextInt();
		// get departure minutes
		depart_min = sc.nextInt();
	
		String tmp = depart_hr +"";
		if(depart_min <=9 )
		{
			tmp += "0" + depart_min + "00";
		}
		else
		{
			tmp += depart_min + "00";
		}
	
		int depart_time = Integer.parseInt(tmp);
		
		String time_depart = Integer.toString(depart_time);
		
		// Formats the time
		String formatTime;
		if(time_depart.length() % 2 == 1)
		{
			formatTime = time_depart.substring(0, 1) + ":" + time_depart.substring(1, 3) + ":" + time_depart.substring(3);
		}
		else
		{
			formatTime = time_depart.substring(0,2) + ":" + time_depart.substring(2,4) + ":" + time_depart.substring(4);
		}
                
	
		System.out.println("Travelling from" + stops.get(start_stop_id).replace("\"", " ") + "to " +
				stops.get(end_stop_id).replace("\"", " ") +  "at " + formatTime);
		System.out.println();
		
		visited.add(start_stop_id);
		
		if(routes.get(start_stop_id).containsKey(depart_time) == false)
		{
			time = routes.get(start_stop_id).ceilingKey(depart_time);
		}
		else
		{
			time = depart_time;
		}
		
		while(routes.get(start_stop_id).get(time) != null)
		{
			for(Stop si: routes.get(start_stop_id).get(time))
			{
				if(si.time >= time)
				{
					Leg start_leg = new Leg (new Stop(start_stop_id, time, si.trip_id), null);
					Leg l = new Leg(si, start_leg);
					queue.add(l);	
				}
			}
			
			if(routes.get(start_stop_id).higherKey(time) != null)
			{
				time = routes.get(start_stop_id).higherKey(time);
			}
			else
			{
				break;
			}
		}
	
		Leg l = queue.remove();
		int currStop = l.this_stop.stop_id;		
		time = l.this_stop.time;
		
		visited.add(currStop);
	
		if(routes.get(currStop).containsKey(time) == false)
			time = routes.get(currStop).ceilingKey(time);
		
		while(currStop != end_stop_id)
		{
			while(routes.containsKey(currStop) && routes.get(currStop).get(time) != null)
			{
				for(Stop sn: routes.get(currStop).get(time))
				{
					if(visited.contains(sn.stop_id) == false)
					{
						if(queuevisited.contains(sn.stop_id) == false)
						{
							Leg temp = new Leg(sn, l);
							queue.add(temp);
							queuevisited.add(sn.stop_id);
						}
					}
				}
				if(routes.get(currStop).higherKey(time) != null)
					time = routes.get(currStop).higherKey(time);
				else
					break;
			}
			queuevisited = new TreeSet<Integer>();

			l = queue.remove();
			currStop = l.this_stop.stop_id;
			time = l.this_stop.time;
				
			visited.add(currStop);
			
			if(routes.containsKey(currStop) && routes.get(currStop).containsKey(time) == false)
			{
				if(routes.get(currStop).higherKey(time) != null)
					time = routes.get(currStop).higherKey(time);
			}
		}
	
		print(l.prev_stop);
		
		String s = stops.get(l.this_stop.stop_id);
		String dtime;
		String tmp1 = Integer.toString(l.this_stop.time);

		if(tmp1.length() % 2 == 1)
		{
			dtime = tmp1.substring(0, 1) + ":" + tmp1.substring(1, 3) + ":" + tmp1.substring(3);
		}
		else
		{
			dtime = tmp1.substring(0,2) + ":" + tmp1.substring(2,4) + ":" + tmp1.substring(4);
		}
		System.out.println("Arrive at" + s.replace("\"", " ") + " at " + dtime);
	}
	
	public String print(Leg l)
	{
		if(l.prev_stop == null)
		{
			String time;
			String tmp = Integer.toString(l.this_stop.time);
			if(tmp.length() % 2 == 1)
				time = tmp.substring(0, 1) + ":" + tmp.substring(1, 3) + ":" + tmp.substring(3);
			else
				time = tmp.substring(0,2) + ":" + tmp.substring(2,4) + ":" + tmp.substring(4);
			Route r = trip.get(l.this_stop.trip_id);
			String s = stops.get(l.this_stop.stop_id);
			System.out.println("Board " + r.name + " at" + s.replace("\"", " ") + "at " + time);
			return r.name;

		}
		else
		{
			String tmp = print(l.prev_stop);
			Route r = trip.get(l.this_stop.trip_id);
			String s = stops.get(l.prev_stop.this_stop.stop_id);
			if(tmp.equals(r.name) == false)
			{
				String time;
				String tmp1 = Integer.toString(l.prev_stop.this_stop.time);
				if(tmp1.length() % 2 == 1)
				{
					time = tmp1.substring(0, 1) + ":" + tmp1.substring(1, 3) + ":" + tmp1.substring(3);
				}
				else
				{
					time = tmp1.substring(0,2) + ":" + tmp1.substring(2,4) + ":" + tmp1.substring(4);
				}
				System.out.println("Transfer to " + r.name + " at" + s.replace("\"", " ") + " at " + time);
				
				return r.name;
			}
			else
			{
				return tmp;
			}
		}		
	}
}
