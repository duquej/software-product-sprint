// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;


public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {  
    long meeting_duration = request.getDuration();
    Collection<String> meeting_attendees = request.getAttendees();

    //First, find all incompatible time ranges so that we can use that as reference
    //for finding time ranges which do actually work.
    ArrayList<TimeRange> incompatible_ranges = findIncompatibleRanges(events, meeting_attendees);

    //Sort the incompatible ranges by start time.
    Collections.sort(incompatible_ranges, TimeRange.ORDER_BY_START);
    Collection<TimeRange> available_ranges = new ArrayList<TimeRange>();

    //Check if there is enough time between the end of the last event and the beginning of the next
    //event. This is initalized to 0 since we are checking at the start of the day.
    int meeting_request_start_time = 0; 
    for (TimeRange event_range : incompatible_ranges){
      int range_end = event_range.end();
      int range_start = event_range.start();
        if (!(meeting_request_start_time + meeting_duration > range_start)){ 
          TimeRange valid_range = TimeRange.fromStartEnd(meeting_request_start_time, range_start, false);
          available_ranges.add(valid_range);
        }
      
      //Handles cases where events overlap and the latest ending event time is actually less than the
      //previous event end time. 
      meeting_request_start_time = Math.max(range_end,meeting_request_start_time);       
    }

    //the potential meeting time of the last possible slot will be whatever time the last event ended up until
    //midnight. 
    if (meeting_request_start_time + meeting_duration <= 24*60){
      TimeRange rest_of_day = TimeRange.fromStartEnd(meeting_request_start_time, 24*60, false);
      available_ranges.add(rest_of_day);
    }

    return available_ranges;
    
  }

  /**
   * Returns a list of TimeRange objects that represent times in which a meeting could not occur. 
   * If a meeting attendee is in one of the {@code events}, then that events time range is incompatible.
   */
  private ArrayList<TimeRange> findIncompatibleRanges(Collection<Event> events, Collection<String> meeting_attendees){   
    ArrayList<TimeRange> incompatible_ranges = new ArrayList<TimeRange>();
    for (Event event : events){
      TimeRange event_range = event.getWhen();
      Collection<String> event_attendees = event.getAttendees();
      if (areMeetingAttendeesInEvent(meeting_attendees,event_attendees)){ 
        incompatible_ranges.add(event_range);
      }
    }

    return incompatible_ranges;
  }


  /**
   * Returns true if there exists at least one meeting attendee from the {@code meeting_attendees} collection
   in the {@code event_attendees} collection. Returns false otherwise.
   */
  private Boolean areMeetingAttendeesInEvent(Collection<String> meeting_attendees, Collection<String> event_attendees){
    for (String attendee : meeting_attendees) {
      if (event_attendees.contains(attendee)){
        return true;
      }      
    }
    return false;
  }

}
