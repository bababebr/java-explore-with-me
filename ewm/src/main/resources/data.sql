INSERT INTO EVENT (annotation, category, created_on, description, event_date, initiator, location, paid,
                   participant_limit, request_moderation, state, title)
VALUES ('q', 1, now(), 'desc', now(), 1, 2, true, 2, true, 'PENDING', 'qqq');

INSERT INTO participant_request (user_id, event_id, status)
VALUES (1, 1, 2);

INSERT INTO participant_request (user_id, event_id, status)
VALUES (2, 1, 2);

INSERT INTO participant_request (user_id, event_id, status)
VALUES (3, 1, 2);

INSERT INTO category (name) values (1);

INSERT INTO users (email, name) VALUES ('email', 'name');

INSERT INTO location (event_id, lat, lon) VALUES (1, 10, 20);

INSERT INTO location (event_id, lat, lon) VALUES (2, 10, 20);