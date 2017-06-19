import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { PrognoseComponent } from './prognose.component';
import { PrognoseDetailComponent } from './prognose-detail.component';
import { PrognosePopupComponent } from './prognose-dialog.component';
import { PrognoseDeletePopupComponent } from './prognose-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class PrognoseResolvePagingParams implements Resolve<any> {

  constructor(private paginationUtil: PaginationUtil) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
      const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
      const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
      return {
          page: this.paginationUtil.parsePage(page),
          predicate: this.paginationUtil.parsePredicate(sort),
          ascending: this.paginationUtil.parseAscending(sort)
    };
  }
}

export const prognoseRoute: Routes = [
  {
    path: 'prognose',
    component: PrognoseComponent,
    resolve: {
      'pagingParams': PrognoseResolvePagingParams
    },
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.prognose.home.title'
    },
    canActivate: [UserRouteAccessService]
  }, {
    path: 'prognose/:id',
    component: PrognoseDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.prognose.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const prognosePopupRoute: Routes = [
  {
    path: 'prognose-new',
    component: PrognosePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.prognose.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: 'prognose/:id/edit',
    component: PrognosePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.prognose.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: 'prognose/:id/delete',
    component: PrognoseDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.prognose.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
