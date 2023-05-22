import React from 'react';
import PropTypes from 'prop-types';
import './friend-request.css';
import {
  useAcceptRequestForMemberIdAndRequestIdMutation,
  useDeclineRequestForMemberIdAndRequestIdMutation,
} from '../../store/api';

const propTypes = {
  request: PropTypes.shape({
    requestId: PropTypes.number.isRequired,
    friend: PropTypes.shape({
      username: PropTypes.string.isRequired,
    }),
  }).isRequired,
};

const FriendRequest = ({ request }) => {
  const [acceptRequest] = useAcceptRequestForMemberIdAndRequestIdMutation();
  const [declineRequest] = useDeclineRequestForMemberIdAndRequestIdMutation();

  return (
    <div className="friend-request">
      <div className="friend-request-text">
        <p className="calendars-request-name">{request.friend.username}</p>
        <p className="calendars-request-name">sent you a request</p>
      </div>
      <div className="calendars-request-buttons">
        <button
          type="button"
          className="calendars-request-button friend-request-accepted"
          onClick={() => acceptRequest({ memberId: 1, requestId: request.requestId })}
        >
          accept
        </button>
        <button
          type="button"
          className="calendars-request-button"
          onClick={() => declineRequest({ memberId: 1, requestId: request.requestId })}
        >
          decline
        </button>
      </div>
    </div>
  );
};

FriendRequest.propTypes = propTypes;

export default FriendRequest;
