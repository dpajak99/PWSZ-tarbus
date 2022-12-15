import { createRouter, createWebHistory } from 'vue-router';

import PageNotFound from '../components/views/404/PageNotFound.vue';

import DashboardRouter from '../components/views/dashboard/DashboardRouter.vue';
import AuthRouter from '../components/views/auth/AuthRouter.vue';

import PanelView from '../components/views/dashboard/panel/PanelView.vue';
import PanelWelcome from '../components/views/dashboard/panel/PanelWelcome.vue';
import PanelMap from '../components/views/dashboard/panel/PanelMap.vue';
import PanelConnectionsSearch from '../components/views/dashboard/panel/PanelConnectionsSearch.vue';
import PanelChat from '../components/views/dashboard/panel/PanelChat.vue';

import AdministrationView from '../components/views/dashboard/administration/AdministrationView.vue';
import AdministrationUsers from '../components/views/dashboard/administration/AdministrationUsers.vue';
import AdministrationReportScheduleUpdate from '../components/views/dashboard/administration/AdministrationReportScheduleUpdate.vue';
import AdministrationSettings from '../components/views/dashboard/administration/AdministrationSettings.vue';
import AdministrationPublishSchedule from '../components/views/dashboard/administration/AdministrationPublishSchedule.vue';

import ApplicationView from '../components/views/dashboard/application/ApplicationView.vue';
import ApplicationDashboard from '../components/views/dashboard/application/ApplicationDashboard.vue';
import ApplicationNewsPanel from '../components/views/dashboard/application/ApplicationNewsPanel.vue';
import ApplicationAppSettings from '../components/views/dashboard/application/ApplicationAppSettings.vue';
import ApplicationFeedback from '../components/views/dashboard/application/ApplicationFeedback.vue';
import ApplicationReportedErrors from '../components/views/dashboard/application/ApplicationReportedErrors.vue';
import ApplicationReportScheduleUpdate from '../components/views/dashboard/application/ApplicationReportScheduleUpdate.vue';
import ApplicationPublishSchedule from '../components/views/dashboard/application/ApplicationPublishSchedule.vue';

import ScheduleView from '../components/views/dashboard/schedule/ScheduleView.vue';
import ScheduleBusLines from '../components/views/dashboard/schedule/ScheduleBusLines.vue';
import ScheduleRoutes from '../components/views/dashboard/schedule/ScheduleRoutes.vue';
import ScheduleSchedule from '../components/views/dashboard/schedule/ScheduleSchedule.vue';
import ScheduleTracksNames from '../components/views/dashboard/schedule/ScheduleTracksNames.vue';
import ScheduleTimetables from '../components/views/dashboard/schedule/ScheduleTimetables.vue';
import ScheduleVersion from '../components/views/dashboard/schedule/ScheduleVersion.vue';
import ScheduleBusStops from '../components/views/dashboard/schedule/ScheduleBusStops.vue';
import ScheduleBusConnections from '../components/views/dashboard/schedule/ScheduleBusConnections.vue';

import LoginView from '../components/views/auth/LoginView.vue';

import SheetRouter from '../components/views/sheets/SheetRouter.vue';
import TrackSheetPage from '../components/views/sheets/TrackSheetPage.vue';
import ScheduleSheetPage from '../components/views/sheets/ScheduleSheetPage.vue';

const routes = [
  // ####
  // Aby nie wpisywać URL - usunąć później
  {
    path: '/',
    redirect: '/dashboard',
  },
  // #####
  {
    path: '/auth',
    name: 'Auth',
    component: AuthRouter,
    redirect: '/auth/login',
    children: [
      { path: 'login', component: LoginView },
      { path: '', redirect: '/auth/login' },
    ],
  },
  // #####
  {
    path: '/sheet',
    name: 'Sheet',
    component: SheetRouter,
    redirect: '/dashboard/panel/welcome',
    children: [
      { path: 'track', name: 'SheetTrack', component: TrackSheetPage },
      { path: 'schedule', name: 'SheetSchedule', component: ScheduleSheetPage },
      { path: '', redirect: '/dashboard/panel/welcome' },
    ],
  },
  // #####
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: DashboardRouter,
    redirect: '/dashboard/panel/welcome',
    children: [
      {
        path: 'panel',
        name: 'Panel',
        component: PanelView,
        children: [
          { path: 'welcome', component: PanelWelcome },
          { path: 'map', component: PanelMap },
          { path: 'connections', component: PanelConnectionsSearch },
          { path: 'chat', component: PanelChat },
          { path: '', redirect: '/dashboard/panel/welcome' },
        ],
      },
      {
        path: 'administration',
        name: 'Administration',
        component: AdministrationView,
        children: [
          { path: 'users', component: AdministrationUsers },
          { path: 'schedule-update', component: AdministrationReportScheduleUpdate },
          { path: 'publish', component: AdministrationPublishSchedule },
          { path: 'settings', component: AdministrationSettings },
          { path: '', redirect: '/dashboard/administration/users' },
        ],
      },
      {
        path: 'application',
        name: 'Application',
        component: ApplicationView,
        children: [
          { path: 'dashboard', component: ApplicationDashboard },
          { path: 'news', component: ApplicationNewsPanel },
          { path: 'app-settings', component: ApplicationAppSettings },
          { path: 'feedback', component: ApplicationFeedback },
          { path: 'reported-errors', component: ApplicationReportedErrors },
          { path: '', redirect: '/dashboard/application/dashboard' },
        ],
      },
      {
        path: 'schedule',
        name: 'Schedule',
        component: ScheduleView,
        children: [
          { path: 'bus-lines', component: ScheduleBusLines },
          { path: 'routes', component: ScheduleRoutes },
          { path: 'schedule', component: ScheduleSchedule },
          { path: 'tracks', component: ScheduleTracksNames },
          { path: 'timetables', component: ScheduleTimetables },
          { path: 'bus-stops', component: ScheduleBusStops },
          { path: 'bus-connections', component: ScheduleBusConnections },
          { path: 'version', component: ScheduleVersion },
          { path: '', redirect: '/dashboard/schedule/bus-lines' },
        ],
      },
    ],
  },
  {
    path: '/:notFound(.*)',
    component: PageNotFound,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  const publicPages = ['/auth/login'];
  const authRequired = !publicPages.includes(to.path);
  const loggedIn = localStorage.getItem('user');
  if (authRequired && !loggedIn) {
    next('/auth/login');
  } else {
    next();
  }
});

export default router;
