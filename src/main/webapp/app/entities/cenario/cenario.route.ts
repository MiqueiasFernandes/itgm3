import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { CenarioComponent } from './cenario.component';
import { CenarioDetailComponent } from './cenario-detail.component';
import { CenarioPopupComponent } from './cenario-dialog.component';
import { CenarioDeletePopupComponent } from './cenario-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class CenarioResolvePagingParams implements Resolve<any> {

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

export const cenarioRoute: Routes = [
  {
    path: 'cenario',
    component: CenarioComponent,
    resolve: {
      'pagingParams': CenarioResolvePagingParams
    },
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.cenario.home.title'
    },
    canActivate: [UserRouteAccessService]
  }, {
    path: 'cenario/:id',
    component: CenarioDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.cenario.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const cenarioPopupRoute: Routes = [
  {
    path: 'cenario-new',
    component: CenarioPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.cenario.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: 'cenario/:id/edit',
    component: CenarioPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.cenario.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: 'cenario/:id/delete',
    component: CenarioDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.cenario.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
