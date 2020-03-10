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
    int MINUTES_IN_DAY = 24 * 60;
    long meetingDuration = request.getDuration();
    Collection<String> meetingAttendees = request.getAttendees();

    //First, find all incompatible time ranges so that we can use that as reference
    //for finding time ranges which do actually work.
    ArrayList<TimeRange> incompatibleRanges = findIncompatibleRanges(events, meetingAttendees);

    //Sort the incompatible ranges by start time.
    Collections.sort(incompatibleRanges, TimeRange.ORDER_BY_START);
    Collection<TimeRange> availableRanges = new ArrayList<TimeRange>();

    //Check if there is enough time between the end of the last event and the beginning of the next
    //event. This is initalized to 0 since we are checking at the start of the day.
    int possibleStartTime  = 0; 
    for (TimeRange eventRange : incompatibleRanges){
      int incompatibleRangeEnd = eventRange.end();
      int incompatibleRangeStart = eventRange.start();
        if (possibleStartTime + meetingDuration <= incompatibleRangeStart){ 
          TimeRange validRange = TimeRange.fromStartEnd(possibleStartTime, incompatibleRangeStart, false);
          availableRanges.add(validRange);
        }
      
      //Handles cases where events overlap and the latest ending event time is actually less than the
      //previous event end time. 
      possibleStartTime = Math.max(incompatibleRangeEnd,possibleStartTime);       
    }

    //the potential meeting time of the last possible slot will be whatever time the last event ended up until
    //midnight. 
    if (possibleStartTime + meetingDuration <= MINUTES_IN_DAY){
      TimeRange restOfDayRange = TimeRange.fromStartEnd(possibleStartTime, MINUTES_IN_DAY, false);
      availableRanges.add(restOfDayRange);
    }

    return availableRanges;
    
  }

  /**
   * Returns a list of TimeRange objects that represent times in which a meeting could not occur. 
   * If a meeting attendee is in one of the {@code events}, then that events time range is incompatible.
   */
  private ArrayList<TimeRange> findIncompatibleRanges(Collection<Event> events, Collection<String> meetingAttendees){   
    ArrayList<TimeRange> incompatibleRanges = new ArrayList<TimeRange>();
    for (Event event : events){
      TimeRange eventRange = event.getWhen();
      Collection<String> eventAttendees = event.getAttendees();
      if (areMeetingAttendeesInEvent(meetingAttendees,eventAttendees)){ 
        incompatibleRanges.add(eventRange);
      }
    }

    return incompatibleRanges;
  }


  /**
   * Returns true if there exists at least one meeting attendee from the {@code meeting_attendees} collection
   * in the {@code event_attendees} collection. Returns false otherwise.
   */
  private Boolean areMeetingAttendeesInEvent(Collection<String> meetingAttendees, Collection<String> eventAttendees){
    for (String attendee : meetingAttendees) {
      if (eventAttendees.contains(attendee)){
        return true;
      }      
    }
    return false;
  }

}
