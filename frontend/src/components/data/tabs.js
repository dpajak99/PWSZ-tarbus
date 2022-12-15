// prettier-ignore
/*eslint-disable */
import {
  TableClock, MonitorEdit, ShieldAccountOutline, Cellphone, Home, MapMarkerRadius, Magnify, Chat,
  BusStop, ChartTimelineVariant, AccountMultiple, MenuRight, Tools, Bus,
  NewspaperVariantMultiple, CommentQuote, AlertCircle, CellphoneCog,
  ViewDashboardOutline, VectorPolylineEdit, BookVariantMultiple, Routes
} from 'mdue';

import {shallowRef} from 'vue';

const tabs = [
  {
    id: 1,
    label: 'panel',
    link: '/dashboard/panel',
    icon: shallowRef(MonitorEdit),
    children: [
      {
        id: 1.1,
        label: 'Welcome',
        link: '/dashboard/panel/welcome',
        icon: Home,
      },
      {
        id: 1.2,
        label: 'Mapa twoich pojazdów',
        link: '/dashboard/panel/map',
        icon: MapMarkerRadius,
      },
      {
        id: 1.3,
        label: 'Wyszukiwanie połączeń',
        link: '/dashboard/panel/connections',
        icon: Magnify,
      },
      {
        id: 1.4,
        label: 'Czat z ekipą tarBUS',
        link: '/dashboard/panel/chat',
        icon: Chat,
      },
    ],
  },
  {
    id: 2,
    label: 'administration',
    link: '/dashboard/administration/users',
    icon: shallowRef(ShieldAccountOutline),
    children: [
      {
        id: 2.1,
        label: 'Wszyscy użytkownicy',
        link: '/dashboard/administration/users',
        icon: AccountMultiple,
      },
    ],
  },
  {
    id: 3,
    label: 'application',
    link: '/dashboard/application',
    icon: shallowRef(Cellphone),
    children: [
      {
        id: 3.1,
        label: 'Dashboard',
        link: '/dashboard/application/dashboard',
        icon: ViewDashboardOutline,
      },
      {
        id: 3.2,
        label: 'Panel newsów',
        link: '/dashboard/application/news',
        icon: NewspaperVariantMultiple,
      },
      {
        id: 3.3,
        label: 'Ustawienia aplikacji',
        link: '/dashboard/application/app-settings',
        icon: CellphoneCog,
      },
      {
        id: 3.4,
        label: 'Feedback',
        link: '/dashboard/application/feedback',
        icon: CommentQuote,
      },
      {
        id: 3.5,
        label: 'Zgłoszone błędy',
        link: '/dashboard/application/reported-errors',
        icon: AlertCircle,
      },
    ],
  },
  {
    id: 4,
    label: 'schedule',
    link: '/dashboard/schedule',
    icon: shallowRef(TableClock),
    children: [
      {
        id: 4.1,
        label: 'Linie autobusowe',
        link: '/dashboard/schedule/bus-lines',
        icon: Bus,
      },
      {
        id: 4.2,
        label: 'Trasy',
        link: '/dashboard/schedule/routes',
        icon: Routes,
      },
      {
        id: 4.3,
        label: 'Rozkład jazdy',
        link: '/dashboard/schedule/schedule',
        icon: TableClock,
      },
      {
        id: 4.4,
        label: 'Nazwy kursów',
        link: '/dashboard/schedule/tracks',
        icon: VectorPolylineEdit,
      },
      {
        id: 4.5,
        label: 'Przystanki autobusowe',
        link: '/dashboard/schedule/bus-stops',
        icon: BusStop,
      },
      {
        id: 4.6,
        label: 'Połączenia przystnków',
        link: '/dashboard/schedule/bus-connections',
        icon: ChartTimelineVariant,
      },
      {
        id: 4.7,
        label: 'Tabliczki przystankowe',
        link: '/dashboard/schedule/timetables',
        icon: BookVariantMultiple,
      },
      {
        id: 4.8,
        label: 'Wersja rozkładu',
        link: '/dashboard/schedule/version',
        icon: Tools,
      },
    ],
  },
];

export default tabs;
