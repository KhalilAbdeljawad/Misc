/*
 * Created by SharpDevelop.
 * User: khali
 * Date: 4/28/2019
 * Time: 8:21 PM
 * 
 * To change this template use Tools | Options | Coding | Edit Standard Headers.
 */
using System;
using System.IO;
using System.Text;
using System.Collections.Generic;


namespace Test
{
	class Program
	{
		public static void Main(string[] args)
		{
			//Input
			//Console.Write("Pleas enter file path: ");
			//string filePath = Console.ReadLine();
			string filePath = "exhibitA-input.csv";
			
			Console.Write("Please enter date: ");
			string date = Console.ReadLine();
			Console.WriteLine("\nProcessing...");
			
			
			
			// to know if the client already listened to song so not to count it again  
			Dictionary<int, HashSet<int>> userSongs = new Dictionary<int, HashSet<int>>();
			
			
			const Int32 BufferSize = 128;
			using (var fileStream = File.OpenRead(filePath))
				using (var streamReader = new StreamReader(fileStream, Encoding.UTF8, true, BufferSize)) {
				String line = streamReader.ReadLine();// to skip firts line (tilte line)
				
				while ((line = streamReader.ReadLine()) != null){
					
					String[] lineParts = line.Split('	');
					
					if(! lineParts[3].StartsWith(date)) continue;
					
					int songId = Convert.ToInt32(lineParts[1]);
					int userId = Convert.ToInt32(lineParts[2]);
					
					if(userSongs.ContainsKey(userId)){
						if(!userSongs[userId].Contains(songId)){
							
							userSongs[userId].Add(songId);
							
						}
					}
					else{
						
						userSongs.Add(userId, new HashSet<int>());
						userSongs[userId].Add(songId);
						
					}
				}

			}

			Dictionary<int, int> output = new Dictionary<int, int>();
			
			// userSong value has the number of distinct songs for current user (Count property),
			// this value used as key to output dictionary and its value is incrementd
			// depending on the key (Count property)
			foreach (KeyValuePair<int, HashSet<int>> userSong in userSongs){

				if(output.ContainsKey(userSong.Value.Count))
					output[userSong.Value.Count] += 1 ;
				else
					output.Add(userSong.Value.Count, 1);
			}
			
			
			
			
			
			using(StreamWriter writer = new StreamWriter("output.csv"))
			{
				writer.WriteLine("DISTINCT_PLAY_COUNT,CLIENT_COUNT");

				foreach (KeyValuePair<int, int> outp in output){
					writer.WriteLine(outp.Key+","+outp.Value);
					
				}
				
			}
			
			System.Diagnostics.Process.Start("output.csv");

			//Console.Write("Press any key to continue . . . ");
			//Console.ReadKey(true);
		}
	}
	
	
}