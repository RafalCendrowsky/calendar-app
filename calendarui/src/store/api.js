import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';

const baseUrl = 'http://localhost:8000/';

const getEncodedCredentials = (state) => btoa(`${state.auth.username}:${state.auth.password}`);

export const api = createApi({
  reducerPath: 'api',
  baseQuery: fetchBaseQuery(
    {
      baseUrl,
      prepareHeaders: (headers, { getState, endpoint }) => {
        if (endpoint !== 'register') {
          headers.set('Authorization', `Basic ${getEncodedCredentials(getState())}`);
        }
        return headers;
      },
    },
  ),
  tagTypes: ['FriendRequests'],
  endpoints: (builder) => ({
    login: builder.query({
      query: () => 'auth/login',
    }),
    getMemberById: builder.query({
      query: (id) => `member/${id}`,
    }),
    getCalendarsForMemberId: builder.query({
      query: (id) => `calendar/member/${id}`,
    }),
    getEventsForMemberIdAndCalendarId: builder.mutation({
      query({ memberId, calendarId }) {
        return {
          url: `member/${memberId}/${calendarId}`,
          method: 'POST',
        };
      },
    }),
    getCalendarByCalendarId: builder.query({
      query: (id) => `calendar/${id}`,
    }),
    getFriendsForMemberId: builder.query({
      query: (id) => `member/${id}/friends`,
      providesTags: () => ['FriendRequests'],
    }),
    acceptRequestForMemberIdAndRequestId: builder.mutation({
      query({ memberId, requestId }) {
        return {
          url: `member/${memberId}/friends/${requestId}`,
          method: 'POST',
        };
      },
      invalidatesTags: ['FriendRequests'],
    }),
    declineRequestForMemberIdAndRequestId: builder.mutation({
      query({ memberId, requestId }) {
        return {
          url: `member/${memberId}/friends/${requestId}`,
          method: 'DELETE',
        };
      },
      invalidatesTags: ['FriendRequests'],
    }),
    register: builder.mutation({
      query: (body) => ({
        url: 'auth/register',
        method: 'POST',
        body,
      }),
    }),
    addEvent: builder.mutation({
      query: (body) => ({
        url: '/events',
        method: 'POST',
        body,
      }),
    }),
    removeEvent: builder.mutation({
      query({ eventId }) {
        return {
          url: '/events',
          method: 'DELETE',
          eventId,
        };
      },
    }),
  }),
});

export const {
  useLazyLoginQuery,
  useRegisterMutation,
  useAddEventMutation,
  useRemoveEventMutation,
  useGetMemberByIdQuery,
  useGetCalendarsForMemberIdQuery,
  useGetCalendarByCalendarIdQuery,
  useGetFriendsForMemberIdQuery,
  useGetEventsForMemberIdAndCalendarIdMutation,
  useAcceptRequestForMemberIdAndRequestIdMutation,
  useDeclineRequestForMemberIdAndRequestIdMutation,
} = api;
