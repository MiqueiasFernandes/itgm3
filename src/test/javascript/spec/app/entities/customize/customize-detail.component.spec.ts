import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { ItgmTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CustomizeDetailComponent } from '../../../../../../main/webapp/app/entities/customize/customize-detail.component';
import { CustomizeService } from '../../../../../../main/webapp/app/entities/customize/customize.service';
import { Customize } from '../../../../../../main/webapp/app/entities/customize/customize.model';

describe('Component Tests', () => {

    describe('Customize Management Detail Component', () => {
        let comp: CustomizeDetailComponent;
        let fixture: ComponentFixture<CustomizeDetailComponent>;
        let service: CustomizeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ItgmTestModule],
                declarations: [CustomizeDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CustomizeService,
                    EventManager
                ]
            }).overrideComponent(CustomizeDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CustomizeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CustomizeService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Customize(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.customize).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
