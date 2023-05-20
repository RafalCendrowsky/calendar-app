import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { useGetCalendarsForMemberIdQuery } from '../../store';
import '../sidebar.css';
import TrashBin from './trash-bin.svg';

const CalendarsSidebar = () => {
  const [selectedCalendarId, setSelectedCalendarId] = useState('');
  const { data, isLoading, error } = useGetCalendarsForMemberIdQuery(1);

  const isSelected = (id) => id === selectedCalendarId;

  // const removeCalendar = () => (<Popup className="popup-remove-calendar">x</Popup>);

  const renderClassName = (calendar) => [
    'nested-sidebar-button',
    isSelected(calendar.id) ? 'selected' : '',
  ].join(' ');

  return (
    <>
      <div className="nested-sidebar">
        <p className="nested-sidebar-title">
          Calendars
        </p>
        <div className="nested-sidebar-list-calendar">
          {!isLoading && !error && data.map((calendar) => (
            <div className="calendar-nav-elem">
              <button
                type="button"
                key={calendar.id}
                className={renderClassName(calendar)}
                onClick={() => setSelectedCalendarId(calendar.id)}
              >
                <Link to={`/calendar/${calendar.id}`} className="nested-sidebar-link">
                  {calendar.name}
                </Link>
              </button>
              <button type="button" className="calendar-nav-elem-remove">
                <img id={`trash${calendar.id.toString()}`} src={TrashBin} alt="X" className="trash-bin-icon" />
              </button>
            </div>
          ))}
        </div>
        <div className="action-btn-calendar">
          <Link to="/calendar/add">
            <button type="button" className="button">Add calendar</button>
          </Link>
        </div>
      </div>
    </>
  );
};

export default CalendarsSidebar;
